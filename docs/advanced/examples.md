# Real-World Examples

This page presents various real-world examples to showcase TorpedoQuery's capabilities in practical scenarios. These examples are designed to demonstrate how to apply TorpedoQuery's features to solve common query requirements.

## Example 1: User Search with Filtering and Pagination

This example demonstrates a user search functionality with multiple filters and pagination:

```java
public class UserSearchService {
    
    private final EntityManager entityManager;
    
    public UserSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public SearchResult<User> searchUsers(UserSearchCriteria criteria) {
        User user = from(User.class);
        
        // Build search condition
        OnGoingLogicalCondition condition = condition();
        
        // Search by name
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            condition.and(
                condition(user.getFirstName()).like().any(criteria.getName())
                .or(user.getLastName()).like().any(criteria.getName())
            );
        }
        
        // Filter by status
        if (criteria.getStatus() != null) {
            condition.and(user.getStatus()).eq(criteria.getStatus());
        }
        
        // Filter by date range
        if (criteria.getCreatedFrom() != null) {
            condition.and(user.getCreatedDate()).gte(criteria.getCreatedFrom());
        }
        if (criteria.getCreatedTo() != null) {
            condition.and(user.getCreatedDate()).lte(criteria.getCreatedTo());
        }
        
        // Apply the condition if not empty
        if (!condition.isEmpty()) {
            where(condition);
        }
        
        // Apply sorting
        if ("nameAsc".equals(criteria.getSort())) {
            orderBy(user.getLastName(), user.getFirstName());
        } else if ("nameDesc".equals(criteria.getSort())) {
            orderBy(desc(user.getLastName()), desc(user.getFirstName()));
        } else if ("dateAsc".equals(criteria.getSort())) {
            orderBy(user.getCreatedDate());
        } else {
            // Default sort by creation date desc
            orderBy(desc(user.getCreatedDate()));
        }
        
        // Create the query
        Query<User> query = select(user);
        
        // Count total results (for pagination)
        Query<Long> countQuery = select(count(user));
        Long totalCount = countQuery.get(entityManager);
        
        // Execute paginated query
        javax.persistence.Query jpaQuery = entityManager.createQuery(query.getQuery());
        
        // Apply parameters
        for (Map.Entry<String, Object> entry : query.getParameters().entrySet()) {
            jpaQuery.setParameter(entry.getKey(), entry.getValue());
        }
        
        // Set pagination
        jpaQuery.setFirstResult(criteria.getPageNumber() * criteria.getPageSize());
        jpaQuery.setMaxResults(criteria.getPageSize());
        
        List<User> results = jpaQuery.getResultList();
        
        return new SearchResult<>(results, totalCount, criteria.getPageNumber(), criteria.getPageSize());
    }
}
```

## Example 2: Complex Reporting Query

This example creates a report with aggregated data across multiple entities:

```java
public class SalesReportingService {
    
    private final EntityManager entityManager;
    
    public SalesReportingService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<SalesReportEntry> generateSalesReport(
            LocalDate fromDate, 
            LocalDate toDate, 
            List<String> productCategories, 
            String region) {
        
        // Define main entities
        Order order = from(Order.class);
        OrderItem item = innerJoin(order.getItems());
        Product product = innerJoin(item.getProduct());
        Customer customer = innerJoin(order.getCustomer());
        
        // Build where conditions
        where(order.getOrderDate()).between(fromDate, toDate);
        
        if (productCategories != null && !productCategories.isEmpty()) {
            where(product.getCategory()).in(productCategories);
        }
        
        if (region != null && !region.isEmpty()) {
            where(customer.getRegion()).eq(region);
        }
        
        // Group by product and month
        groupBy(product.getId(), product.getName(), product.getCategory())
            .having(sum(item.getQuantity())).gt(0);
        
        // Order by product category and total revenue
        orderBy(product.getCategory(), desc(sum(operation(item.getQuantity()).multiply(item.getUnitPrice()))));
        
        // Select the report data
        Query<Object[]> query = select(
            product.getId(),
            product.getName(),
            product.getCategory(),
            sum(item.getQuantity()),
            sum(operation(item.getQuantity()).multiply(item.getUnitPrice())),
            avg(item.getUnitPrice())
        );
        
        // Execute the query
        List<Object[]> results = query.list(entityManager);
        
        // Map results to report entries
        return results.stream()
            .map(row -> new SalesReportEntry(
                (String) row[0],  // productId
                (String) row[1],  // productName
                (String) row[2],  // category
                (Long) row[3],    // totalQuantity
                (BigDecimal) row[4], // totalRevenue
                (BigDecimal) row[5]  // avgPrice
            ))
            .collect(Collectors.toList());
    }
}
```

## Example 3: Hierarchical Data Query

This example demonstrates working with hierarchical data (organization structure):

```java
public class OrganizationService {
    
    private final EntityManager entityManager;
    
    public OrganizationService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<DepartmentDTO> getDepartmentHierarchy(String topLevelDepartmentCode) {
        // Get the top-level department
        Department topDept = from(Department.class);
        where(topDept.getCode()).eq(topLevelDepartmentCode);
        Department topLevelDept = select(topDept).get(entityManager);
        
        // Build the hierarchy
        return buildDepartmentHierarchy(topLevelDept.getId());
    }
    
    private List<DepartmentDTO> buildDepartmentHierarchy(String parentId) {
        // Get direct child departments
        Department dept = from(Department.class);
        where(dept.getParentId()).eq(parentId);
        orderBy(dept.getName());
        List<Department> departments = select(dept).list(entityManager);
        
        // Transform to DTOs and recursively get children
        return departments.stream()
            .map(d -> {
                DepartmentDTO dto = new DepartmentDTO(
                    d.getId(), 
                    d.getCode(),
                    d.getName(),
                    d.getLevel()
                );
                
                // Get employees in this department
                Employee emp = from(Employee.class);
                where(emp.getDepartmentId()).eq(d.getId());
                where(emp.getStatus()).eq(EmployeeStatus.ACTIVE);
                orderBy(emp.getLastName(), emp.getFirstName());
                List<Employee> employees = select(emp).list(entityManager);
                
                dto.setEmployees(employees.stream()
                    .map(e -> new EmployeeDTO(
                        e.getId(), 
                        e.getFirstName(), 
                        e.getLastName(), 
                        e.getPosition())
                    )
                    .collect(Collectors.toList()));
                
                // Recursively get child departments
                dto.setChildDepartments(buildDepartmentHierarchy(d.getId()));
                
                return dto;
            })
            .collect(Collectors.toList());
    }
}
```

## Example 4: Complex Data Filtering with Geographic Location

This example performs complex filtering including geographic proximity:

```java
public class PropertySearchService {
    
    private final EntityManager entityManager;
    private final LocationService locationService;
    
    public PropertySearchService(EntityManager entityManager, LocationService locationService) {
        this.entityManager = entityManager;
        this.locationService = locationService;
    }
    
    public List<Property> findProperties(PropertySearchCriteria criteria) {
        Property property = from(Property.class);
        PropertyFeatures features = innerJoin(property.getFeatures());
        
        // Base conditions
        where(property.getStatus()).eq(PropertyStatus.AVAILABLE);
        
        // Price range
        if (criteria.getMinPrice() != null) {
            where(property.getPrice()).gte(criteria.getMinPrice());
        }
        if (criteria.getMaxPrice() != null) {
            where(property.getPrice()).lte(criteria.getMaxPrice());
        }
        
        // Property type
        if (criteria.getPropertyTypes() != null && !criteria.getPropertyTypes().isEmpty()) {
            where(property.getType()).in(criteria.getPropertyTypes());
        }
        
        // Minimum bedrooms and bathrooms
        if (criteria.getMinBedrooms() != null) {
            where(property.getBedrooms()).gte(criteria.getMinBedrooms());
        }
        if (criteria.getMinBathrooms() != null) {
            where(property.getBathrooms()).gte(criteria.getMinBathrooms());
        }
        
        // Specific amenities (using WITH clause on the features join)
        if (criteria.getRequiredAmenities() != null && !criteria.getRequiredAmenities().isEmpty()) {
            OnGoingLogicalCondition amenitiesCondition = condition();
            for (String amenity : criteria.getRequiredAmenities()) {
                amenitiesCondition.or(features.getFeatureType()).eq(amenity);
            }
            with(amenitiesCondition);
        }
        
        // Location-based filtering
        if (criteria.getCity() != null && !criteria.getCity().isEmpty()) {
            where(property.getCity()).eq(criteria.getCity());
        }
        if (criteria.getZipCode() != null && !criteria.getZipCode().isEmpty()) {
            where(property.getZipCode()).eq(criteria.getZipCode());
        }
        
        // Execute query
        Query<Property> query = select(property);
        List<Property> results = query.list(entityManager);
        
        // Post-processing for distance-based filtering (if coordinates provided)
        if (criteria.getLatitude() != null && criteria.getLongitude() != null && criteria.getMaxDistanceInMiles() != null) {
            return results.stream()
                .filter(p -> locationService.calculateDistanceMiles(
                    criteria.getLatitude(), criteria.getLongitude(),
                    p.getLatitude(), p.getLongitude()) <= criteria.getMaxDistanceInMiles())
                .collect(Collectors.toList());
        }
        
        return results;
    }
}
```

## Example 5: Comprehensive Usage Example from ComplexQueryExample

This example comes from the source code and demonstrates how to build a query with multiple joins and conditions:

```java
public class ComplexQueryExample {

    private EntityManager manager;

    public List<User> findUsers() {
        User from = from(User.class);
        City city = innerJoin(from.getCity());
        with(city.getCode()).in("one", "two").or(city.getCode()).notIn("three", "four");
        District district = innerJoin(city.getDistrict());
        with(district.getCode()).notIn("exclude1", "exclude2");
        State state = innerJoin(district.getState());
        with(state.getCode()).eq("AP").or(state.getCode()).eq("GUJ").or(state.getCode()).eq("KTK");
        with(state.getCountry().getCode()).eq("india");

        return select(from).list(manager);
    }
}
```

This query:
1. Starts with the User entity
2. Joins with City, District, and State entities in a chain
3. Applies various conditions using WITH clauses on each join
4. Creates complex OR conditions for state codes
5. Accesses nested properties (state.getCountry().getCode())
6. Returns a list of User objects that match all conditions

## Example 6: Advanced Dynamic Criteria Builder

Here's an example of a reusable criteria builder pattern with TorpedoQuery:

```java
public class GenericSearchService<T> {
    
    private final EntityManager entityManager;
    private final Class<T> entityClass;
    
    public GenericSearchService(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }
    
    public List<T> search(List<SearchCriterion> criteria, List<SortCriterion> sortings, int maxResults) {
        T entity = from(entityClass);
        
        // Build the WHERE clause from generic criteria
        if (criteria != null && !criteria.isEmpty()) {
            OnGoingLogicalCondition mainCondition = condition();
            for (SearchCriterion criterion : criteria) {
                applySearchCriterion(mainCondition, entity, criterion);
            }
            where(mainCondition);
        }
        
        // Apply sorting
        if (sortings != null && !sortings.isEmpty()) {
            List<Object> sortFields = new ArrayList<>();
            for (SortCriterion sort : sortings) {
                // Use reflection to get the appropriate getter
                try {
                    String getterName = "get" + StringUtils.capitalize(sort.getFieldName());
                    Method getterMethod = entityClass.getMethod(getterName);
                    Object fieldValue = getterMethod.invoke(entity);
                    
                    if ("DESC".equalsIgnoreCase(sort.getDirection())) {
                        sortFields.add(desc(fieldValue));
                    } else {
                        sortFields.add(fieldValue);
                    }
                } catch (Exception e) {
                    // Log warning and continue
                    logger.warn("Could not sort by field: " + sort.getFieldName(), e);
                }
            }
            
            if (!sortFields.isEmpty()) {
                orderBy(sortFields.toArray());
            }
        }
        
        // Execute query with limit
        Query<T> query = select(entity);
        javax.persistence.Query jpaQuery = entityManager.createQuery(query.getQuery());
        
        // Apply parameters
        for (Map.Entry<String, Object> entry : query.getParameters().entrySet()) {
            jpaQuery.setParameter(entry.getKey(), entry.getValue());
        }
        
        if (maxResults > 0) {
            jpaQuery.setMaxResults(maxResults);
        }
        
        return jpaQuery.getResultList();
    }
    
    private void applySearchCriterion(OnGoingLogicalCondition condition, T entity, SearchCriterion criterion) {
        try {
            // Get the field value using reflection
            String getterName = "get" + StringUtils.capitalize(criterion.getFieldName());
            Method getterMethod = entityClass.getMethod(getterName);
            Object fieldValue = getterMethod.invoke(entity);
            
            // Apply appropriate operator
            switch (criterion.getOperator()) {
                case EQUALS:
                    condition.and(fieldValue).eq(criterion.getValue());
                    break;
                case LIKE:
                    condition.and(fieldValue).like().any(criterion.getValue().toString());
                    break;
                case GREATER_THAN:
                    condition.and(fieldValue).gt(criterion.getValue());
                    break;
                case LESS_THAN:
                    condition.and(fieldValue).lt(criterion.getValue());
                    break;
                case IN:
                    if (criterion.getValue() instanceof Collection) {
                        condition.and(fieldValue).in((Collection<?>) criterion.getValue());
                    }
                    break;
                case IS_NULL:
                    condition.and(fieldValue).isNull();
                    break;
                case IS_NOT_NULL:
                    condition.and(fieldValue).isNotNull();
                    break;
                // Add other operators as needed
            }
        } catch (Exception e) {
            // Log warning and continue
            logger.warn("Could not apply criterion for field: " + criterion.getFieldName(), e);
        }
    }
}
```

This generic search service demonstrates how TorpedoQuery can be used to build a flexible, reusable search component that can work with any entity type and support various filtering and sorting options.
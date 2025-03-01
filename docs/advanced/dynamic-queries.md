# Dynamic Queries

One of TorpedoQuery's strongest features is the ability to build queries dynamically at runtime. This is especially useful for creating search screens, implementing flexible filtering, and adapting queries based on user input.

## Conditional Clause Addition

The most basic form of dynamic query building is conditionally adding WHERE clauses:

```java
Entity entity = from(Entity.class);

// Only add this condition if the parameter is provided
String name = getUserInput();
if (name != null && !name.isEmpty()) {
    where(entity.getName()).eq(name);
}

// Add another condition if needed
Integer minValue = getMinValueInput();
if (minValue != null) {
    where(entity.getIntegerField()).gte(minValue);
}

Query<Entity> query = select(entity);
```

This results in a query that dynamically includes only the conditions for which values were provided.

## Using Empty Conditions

TorpedoQuery allows you to create empty condition placeholders that can be filled later:

```java
Entity entity = from(Entity.class);
OnGoingLogicalCondition emptyCondition = condition();
Query<Entity> query = select(entity);

// Later in your code, you can add to this condition
if (shouldFilterByName()) {
    emptyCondition.and(entity.getName()).eq("test");
}

if (shouldFilterByCode()) {
    emptyCondition.and(entity.getCode()).like().startsWith("ABC");
}

// Finally, apply the condition if it's not empty
if (!emptyCondition.isEmpty()) {
    where(emptyCondition);
}
```

If no conditions are added to the empty condition, no WHERE clause will be added to the query.

## Testing If Conditions Have Been Added

You can check if conditions have already been added to your query:

```java
Entity entity = from(Entity.class);
Query<Entity> query = select(entity);

// Check if conditions exist
Optional<OnGoingLogicalCondition> existingCondition = query.condition();
if (existingCondition.isPresent()) {
    // Add to existing condition
    existingCondition.get().and(entity.getCode()).eq("newCode");
} else {
    // Create new condition
    where(entity.getCode()).eq("newCode");
}
```

## Building Complex Dynamic Conditions

For more complex scenarios, you can build condition groups:

```java
Entity entity = from(Entity.class);

// Create a condition group for status filters
OnGoingLogicalCondition statusConditions = condition();
if (includeActive()) {
    statusConditions.or(entity.isActive()).eq(true);
}
if (includePending()) {
    statusConditions.or(entity.getStatus()).eq("PENDING");
}

// Create a condition group for date filters
OnGoingLogicalCondition dateConditions = condition();
if (startDate != null) {
    dateConditions.and(entity.getDateField()).gte(startDate);
}
if (endDate != null) {
    dateConditions.and(entity.getDateField()).lte(endDate);
}

// Combine the condition groups
if (!statusConditions.isEmpty() && !dateConditions.isEmpty()) {
    where(statusConditions).and(dateConditions);
} else if (!statusConditions.isEmpty()) {
    where(statusConditions);
} else if (!dateConditions.isEmpty()) {
    where(dateConditions);
}

Query<Entity> query = select(entity);
```

## Dynamic Joins

You can also dynamically add joins based on requirements:

```java
Entity entity = from(Entity.class);

// Only join with sub-entities if we need them
if (needSubEntityData()) {
    SubEntity subEntity = innerJoin(entity.getSubEntities());
    
    // Add conditions on the joined entity
    if (subEntityCode != null) {
        with(subEntity.getCode()).eq(subEntityCode);
    }
}

Query<Entity> query = select(entity);
```

## Dynamic Select Clauses

Create dynamic SELECT clauses to determine what fields to return:

```java
Entity entity = from(Entity.class);
List<Object> selectFields = new ArrayList<>();

// Always select the ID
selectFields.add(entity.getId());

// Conditionally add other fields
if (includeBasicInfo()) {
    selectFields.add(entity.getName());
    selectFields.add(entity.getCode());
}

if (includeMetrics()) {
    selectFields.add(entity.getIntegerField());
    selectFields.add(entity.getBigDecimalField());
}

// Convert to array
Object[] selectArray = selectFields.toArray();

// Create query with dynamic select fields
Query<Object[]> query = select(selectArray);
```

## Dynamic Sorting

Add sorting criteria dynamically:

```java
Entity entity = from(Entity.class);

// Get sort field and direction from user
String sortField = getSortField(); // e.g. "name", "code", "date"
String sortDirection = getSortDirection(); // "asc" or "desc"

// Apply sort based on user input
if ("name".equals(sortField)) {
    if ("desc".equals(sortDirection)) {
        orderBy(desc(entity.getName()));
    } else {
        orderBy(asc(entity.getName()));
    }
} else if ("code".equals(sortField)) {
    if ("desc".equals(sortDirection)) {
        orderBy(desc(entity.getCode()));
    } else {
        orderBy(asc(entity.getCode()));
    }
} else if ("date".equals(sortField)) {
    if ("desc".equals(sortDirection)) {
        orderBy(desc(entity.getDateField()));
    } else {
        orderBy(asc(entity.getDateField()));
    }
}

Query<Entity> query = select(entity);
```

## Search Form Example

Here's a comprehensive example of implementing a search form:

```java
public List<Entity> search(SearchForm form) {
    Entity entity = from(Entity.class);
    
    // Start with an empty condition
    OnGoingLogicalCondition searchCondition = condition();
    
    // Add text search if provided
    if (form.getSearchText() != null && !form.getSearchText().isEmpty()) {
        String searchPattern = "%" + form.getSearchText() + "%";
        OnGoingLogicalCondition textSearch = condition(entity.getName()).like(searchPattern)
            .or(entity.getCode()).like(searchPattern);
        searchCondition.and(textSearch);
    }
    
    // Add date range if provided
    if (form.getStartDate() != null) {
        searchCondition.and(entity.getDateField()).gte(form.getStartDate());
    }
    if (form.getEndDate() != null) {
        searchCondition.and(entity.getDateField()).lte(form.getEndDate());
    }
    
    // Add status filter if provided
    if (form.getStatusList() != null && !form.getStatusList().isEmpty()) {
        searchCondition.and(entity.getStatus()).in(form.getStatusList());
    }
    
    // Apply the condition if not empty
    if (!searchCondition.isEmpty()) {
        where(searchCondition);
    }
    
    // Add sorting
    if ("dateDesc".equals(form.getSortOption())) {
        orderBy(desc(entity.getDateField()));
    } else if ("dateAsc".equals(form.getSortOption())) {
        orderBy(asc(entity.getDateField()));
    } else if ("nameAsc".equals(form.getSortOption())) {
        orderBy(asc(entity.getName()));
    } else {
        // Default sorting
        orderBy(desc(entity.getDateField()));
    }
    
    // Create and execute the query
    Query<Entity> query = select(entity);
    return query.list(entityManager);
}
```

## Advanced Example: Dynamic Report Builder

Here's an advanced example that builds a complex report query dynamically:

```java
public List<ReportData> generateReport(ReportSettings settings) {
    Entity entity = from(Entity.class);
    
    // Dynamic joins based on included data
    SubEntity subEntity = null;
    if (settings.includeSubEntityData()) {
        subEntity = leftJoin(entity.getSubEntities());
    }
    
    // Build select fields based on requested columns
    List<Object> selectFields = new ArrayList<>();
    for (String column : settings.getColumns()) {
        switch (column) {
            case "id":
                selectFields.add(entity.getId());
                break;
            case "name":
                selectFields.add(entity.getName());
                break;
            case "active":
                selectFields.add(entity.isActive());
                break;
            case "subEntityCount":
                if (subEntity != null) {
                    selectFields.add(count(subEntity));
                }
                break;
            case "avgValue":
                if (subEntity != null) {
                    selectFields.add(avg(subEntity.getNumberField()));
                }
                break;
            // Add other columns as needed
        }
    }
    
    // Add conditions based on filters
    OnGoingLogicalCondition filterCondition = condition();
    for (ReportFilter filter : settings.getFilters()) {
        switch (filter.getField()) {
            case "name":
                addStringFilter(filterCondition, entity.getName(), filter);
                break;
            case "active":
                addBooleanFilter(filterCondition, entity.isActive(), filter);
                break;
            case "date":
                addDateFilter(filterCondition, entity.getDateField(), filter);
                break;
            case "value":
                addNumericFilter(filterCondition, entity.getIntegerField(), filter);
                break;
            // Add other filters as needed
        }
    }
    
    if (!filterCondition.isEmpty()) {
        where(filterCondition);
    }
    
    // Add grouping if needed for aggregations
    if (settings.isAggregated()) {
        groupBy(entity.getId(), entity.getName(), entity.isActive());
    }
    
    // Add sorting
    for (ReportSorting sort : settings.getSorting()) {
        switch (sort.getField()) {
            case "name":
                addSorting(entity.getName(), sort.getDirection());
                break;
            case "date":
                addSorting(entity.getDateField(), sort.getDirection());
                break;
            // Add other sort fields as needed
        }
    }
    
    // Create and execute query
    Query<Object[]> query = select(selectFields.toArray());
    List<Object[]> results = query.list(entityManager);
    
    // Convert results to report data objects
    return convertToReportData(results, settings.getColumns());
}

// Helper methods for the report builder
private void addStringFilter(OnGoingLogicalCondition condition, String field, ReportFilter filter) {
    // Implementation details...
}

private void addSorting(Object field, String direction) {
    if ("desc".equals(direction)) {
        orderBy(desc(field));
    } else {
        orderBy(asc(field));
    }
}
```

Dynamic query building with TorpedoQuery enables you to create powerful, flexible queries that adapt to user input while maintaining type safety and clean code structure.
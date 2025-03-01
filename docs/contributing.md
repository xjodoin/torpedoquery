# Contributing to TorpedoQuery

Thank you for your interest in contributing to TorpedoQuery! This document provides guidelines and information about contributing to the project.

## Getting Started

### Setting Up Your Development Environment

1. **Fork the Repository**: Start by forking [xjodoin/torpedoquery](https://github.com/xjodoin/torpedoquery) on GitHub.

2. **Clone Your Fork**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/torpedoquery.git
   cd torpedoquery
   ```

3. **Set Up Remote**:
   ```bash
   git remote add upstream https://github.com/xjodoin/torpedoquery.git
   ```

4. **Install Dependencies**:
   TorpedoQuery uses Maven for dependency management. To build the project:
   ```bash
   mvn clean install
   ```

### Development Workflow

1. **Create a Branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Your Changes**: Implement your changes, following the coding conventions.

3. **Write Tests**: Add or update tests to cover your changes.

4. **Run Tests**:
   ```bash
   mvn test
   ```

5. **Submit a Pull Request**: 
   - Push your changes to your fork
   - Create a pull request from your branch to the main repository's master branch
   - Provide a clear description of the changes and reference any related issues

## Coding Conventions

### Java Code Style

- Use 4 spaces for indentation, not tabs.
- Follow standard Java naming conventions:
  - Class names should be `CamelCase`
  - Method names should be `camelCase`
  - Constants should be `UPPER_CASE_WITH_UNDERSCORES`
- Add appropriate Javadoc comments for public APIs.
- Keep methods focused on a single responsibility.
- Use meaningful variable and method names.

### Test Conventions

- Write unit tests for new functionality.
- Ensure all tests pass before submitting a pull request.
- Tests should be named descriptively, indicating what is being tested.
- Place tests in the corresponding package under the `src/test` directory.

## Pull Request Process

1. **Update Documentation**: Update any relevant documentation, including Javadoc, README, and this documentation site if needed.

2. **Pass All Tests**: Ensure all tests pass and that there are no conflicts with the base branch.

3. **Code Review**: A project maintainer will review your code. Be prepared to make changes if requested.

4. **Merge**: Once approved, a maintainer will merge your pull request.

## Reporting Issues

When reporting issues, please include:

- A clear description of the issue
- Steps to reproduce
- Expected behavior
- Actual behavior
- Your environment (Java version, operating system, etc.)
- If possible, a minimal code example that demonstrates the issue

## Feature Requests

Feature requests are welcome! When submitting a feature request:

- Clearly describe the feature and its use case
- Explain why it would be beneficial to the project
- If possible, provide examples of how the feature might be used

## Community Guidelines

- Be respectful and considerate of others.
- Keep discussions focused on TorpedoQuery development.
- Help others when you can.
- Give constructive feedback and be open to receiving it.

## License

By contributing to TorpedoQuery, you agree that your contributions will be licensed under the project's [Apache License 2.0](https://github.com/xjodoin/torpedoquery/blob/master/LICENSE).

## Security Issues

To report security vulnerabilities, please use the [Tidelift security contact](https://tidelift.com/security) instead of the public issue tracker. Tidelift will coordinate the fix and disclosure.

## Contact

If you have any questions about contributing that aren't answered here, feel free to open an issue for discussion.

Thank you for contributing to TorpedoQuery and helping to make it better!
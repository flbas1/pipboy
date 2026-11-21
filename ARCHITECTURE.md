# PipBoy Architecture

## 1. Overview

PipBoy is a configuration-driven web application designed to deliver customizable, interactive experiences through a lightweight web interface and a Java/Spring Boot backend.

Originally developed as a themed event application, PipBoy is designed around a broader concept: a single application can support different experiences through configuration rather than requiring separate application implementations.

The application separates its core functionality from event-specific content, presentation, and external integrations.

The guiding principle is:

**Configure the experience. Don't hardcode the experience.**

## 2. Technology Stack

| Component | Technology |
|---|---|
| Backend | Java 21 |
| Application framework | Spring Boot 3.5 |
| Build | Maven |
| Frontend | HTML, CSS, JavaScript |
| Configuration | JSON and YAML |
| External integrations | REST APIs |
| Deployment | Linux / Debian |

The application intentionally avoids unnecessary framework and infrastructure dependencies. Its current requirements are well served by a lightweight Spring Boot application and a static frontend.

## 3. Architectural Principles

### Configuration over hardcoding

Application behavior and event-specific content should be driven by external configuration wherever practical.

Profiles define the experience without requiring application code changes.

Configuration should be:
- External to the application binary.
- Human-readable and maintainable.
- Validated when loaded.
- Independent of the underlying application implementation.
- Capable of supporting multiple installations.

### Separation of concerns

The application separates responsibilities into distinct layers:

- **Controllers:** Handle HTTP requests and responses.
- **Services:** Implement application behavior and business logic.
- **Models:** Represent application data and configuration.
- **Integrations:** Communicate with external services.
- **Frontend:** Presents the experience and handles user interaction.

Controllers should remain thin. Integration-specific communication should not leak into unrelated application components.

### Minimize unnecessary complexity

PipBoy is intentionally a relatively small application.

Architectural decisions should solve actual requirements rather than introduce abstractions for hypothetical future problems.

The objective is maintainability and extensibility without unnecessary infrastructure.

## 4. Application Structure

The current Java implementation follows a conventional Spring Boot structure:

```text
src/main/java/flbas1/pipboy/
├── PipBoyApplication.java
├── controller/
│   ├── MenuController.java
│   ├── ProfileController.java
│   └── RadioMAController.java
├── model/
│   ├── MenuProfile.java
│   └── PipBoyConfig.java
└── service/
    ├── MenuService.java
    ├── PipBoyConfigService.java
    └── RadioMAService.java

src/main/resources/
├── application.properties
├── application-local.properties
├── pipboy/
│   └── menu.yml
├── profiles/
│   ├── profiles.json
│   ├── birthday.json
│   └── birthday.private.json
├── radio.json
└── static/
    ├── index.html
    ├── pipboy.js
    ├── pipboy.css
    └── icons/
```

This structure reflects the current implementation. Configuration externalization and further separation of integration responsibilities are ongoing architectural goals.

## 5. Profiles and Configuration

Profiles define the content and characteristics of an individual PipBoy experience.

The current implementation uses a profile registry to identify available profiles and loads the corresponding profile data at runtime.

Private configuration can be supplied through a separate profile overlay, allowing sensitive or installation-specific values to remain separate from public content.

The intended configuration model separates:

- **Application configuration:** Runtime behavior and application settings.
- **Profile configuration:** Event-specific content and functionality.
- **Presentation configuration:** Themes, navigation, and visual identity.
- **Integration configuration:** Connections and credentials for external services.

Configuration should not require rebuilding the application.

A future objective is to establish a consistent YAML-based configuration contract with startup validation.

## 6. Backend Architecture

Spring Boot provides the application runtime and REST API infrastructure.

Controllers expose application capabilities through HTTP endpoints.

Services encapsulate application behavior and coordinate access to configuration and external integrations.

This separation provides a clear boundary between HTTP concerns and application logic.

The backend should remain independent of the presentation layer. The same application services should be usable regardless of whether requests originate from the bundled frontend or another supported client.

## 7. External Integrations

PipBoy can integrate with external services to extend its capabilities.

Music Assistant is an example of an external integration.

Integrations should be isolated behind service-level interfaces so that:

- External API details remain contained.
- Authentication is managed independently of application logic.
- Network failures do not unnecessarily compromise unrelated features.
- Integrations can be enabled or disabled through configuration.
- External services can be replaced without rewriting the application.

Integration availability should be treated as an external dependency, not an assumption.

## 8. Frontend Architecture

The frontend currently uses native HTML, CSS, and JavaScript.

This intentionally lightweight approach avoids introducing a separate frontend build ecosystem for an application whose primary requirements are configuration-driven rendering and interactive controls.

The frontend communicates with the backend through REST endpoints.

Content should be rendered from backend-provided data rather than duplicated in static markup.

The frontend also supports Progressive Web App capabilities, including a web manifest, service worker, and locally cached content.

Offline functionality is intended to preserve useful application capabilities when network connectivity is unavailable.

## 9. Security and Configuration Management

Credentials and environment-specific settings must not be committed to source control.

External service credentials should be supplied through environment variables or protected external configuration.

The application should:
- Avoid exposing integration credentials through API responses.
- Validate external integration inputs.
- Restrict outbound requests to configured services.
- Validate user-supplied paths and resource identifiers.
- Keep private profile information separate from public content.
- Avoid exposing sensitive configuration through logs or diagnostics.

Local development configuration should remain outside the tracked application configuration.

## 10. Deployment

PipBoy is designed to run as a lightweight application on Linux.

The deployment model favors a single application artifact with installation-specific configuration supplied externally.

This allows multiple installations to share the same application version while maintaining independent profiles, credentials, and integrations.

Deployment should be reproducible, with build and validation steps automated through CI/CD.

## 11. Engineering Roadmap

The following items represent architectural objectives rather than claims of completed functionality.

| Area | Objective |
|---|---|
| Configuration | Externalize runtime configuration |
| Profiles | Support configurable profile selection |
| Validation | Validate configuration at startup |
| Presentation | Make navigation and themes configuration-driven |
| Integrations | Isolate external service communication |
| Resilience | Handle unavailable integrations gracefully |
| Security | Centralize secret management |
| Testing | Add unit and integration test coverage |
| CI/CD | Automate build and test validation |
| Documentation | Document API contracts and architectural decisions |

## 12. Design Philosophy

PipBoy is an exercise in building adaptable software without unnecessary complexity.

The objective is not to create a framework for every possible application. It is to demonstrate how a well-structured application can support changing requirements through configuration, clearly defined responsibilities, and isolated dependencies.

The application should be easy to understand, easy to deploy, and easy to extend.

**One application. Different experiences. Configuration drives the difference.**
# PipBoy

Parties are a wonderful time of the year!

Family and friends love themed parties, and this project is a way to make the party itself part of the theme.

**PipBoy** is a reusable party-management and entertainment system presented through a themed web interface. The first deployment is a Fallout-inspired birthday party, but the underlying application is designed to support many different holidays and events.

The goal is simple:

> **Make the party easier to run while making the technology part of the experience.**

---

## What It Does

The PipBoy system is built around three core party features.

### 🍔 Menu

A themed food and beverage menu displayed through the party interface.

Menu content is configurable by party profile rather than being hard-coded into the application.

For Fallout parties, a "ghoul" menu assistant should help create items that are appropriate to the Fallout universe.

Examples:

* Brahmin Burgers
* BlamCo Mac & Cheese
* Mirelurk Crab Cakes
* Sugar Bomb Carrots
* Nuka-Cola

For other themes, the menu should change to match the event.

### 📻 Music

A party radio/music system that allows the display to show what is currently playing and eventually control the party music.

The planned integration is:

```text
PipBoy
   ↓
Home Assistant
   ↓
Music Assistant
   ↓
Party Music
```

### 📸 Pictures

A photo experience for displaying pictures throughout the party.

Future versions may support:

* photo slideshows
* themed transitions
* party photo sources
* integration with other photo services

---

# Themes

The PipBoy application is **not a Fallout application**.

Fallout is simply the first theme.

Each theme should provide its own visual identity while using the same underlying application.

## Fallout

The original Pip-Boy experience.

```text
fallout/
├── profile
└── fallout.css
```

Visual characteristics:

* Pip-Boy green
* CRT scanlines
* screen flicker
* phosphor glow
* Fallout terminology
* Fallout-inspired menus
* Fallout radio

The first real deployment is the **bdaygirl** Fallout party profile.

---

## Thanksgiving

Thanksgiving should move completely away from the Fallout visual language.

```text
thanksgiving/
├── profile
└── thanksgiving.css
```

Visual characteristics:

* warm brown tones
* amber/orange accents
* autumn colors
* leaves
* cozy lighting
* Thanksgiving-themed menus
* Thanksgiving music

The theme should feel warm and seasonal rather than like a green terminal.

---

## Christmas

Christmas gets its own visual identity.

```text
christmas/
├── profile
└── christmas.css
```

Visual characteristics:

* Christmas colors
* snowfall
* Christmas lights
* glowing decorations
* holiday menus
* Christmas music

Snow should be generated with CSS/HTML effects rather than requiring a collection of static images.

---

## July 4th

July 4th gets another completely different presentation.

```text
july4/
├── profile
└── july4.css
```

Visual characteristics:

* red
* white
* blue
* stars
* patriotic styling
* animated fireworks
* summer party menus
* appropriate music

The fireworks should be generated using CSS animations where practical.

---

# Architecture

The most important architectural principle is that **the application should not care which theme is currently active**.

The theme is configuration.

```text
                         ┌───────────────────┐
                         │      Browser      │
                         │    pip.local      │
                         └─────────┬─────────┘
                                   │
                                   ▼
                         ┌───────────────────┐
                         │    Spring Boot    │
                         │                   │
                         │ Controllers       │
                         │ Services          │
                         │ Profiles          │
                         │ Theme Selection   │
                         └─────────┬─────────┘
                                   │
                     ┌─────────────┼─────────────┐
                     │             │             │
                     ▼             ▼             ▼
                  Profiles       Themes      Integrations
                     │             │             │
                     ▼             ▼             ▼
                   JSON           CSS       Home Assistant
                                             Music Assistant
```

The same application should be capable of loading:

```text
Fallout
Thanksgiving
Christmas
July 4th
```

without changing the business logic.

---

# Profiles

Party-specific information belongs in profiles.

For example:

```text
profiles/
├── bdaygirl.json
├── thanksgiving.json
├── christmas.json
├── july4.json
└── default.json
```

A profile can eventually define:

* party name
* theme
* menu
* music station
* photo source
* visual effects
* party-specific settings

Example:

```json
{
  "id": "christmas",
  "theme": "christmas",
  "menu": {},
  "radio": {},
  "photos": {}
}
```

---

# Theme Architecture

A profile selects a theme.

The frontend then loads the appropriate visual resources.

Conceptually:

```text
Profile
   │
   ├── theme = fallout
   │       └── fallout.css
   │
   ├── theme = thanksgiving
   │       └── thanksgiving.css
   │
   ├── theme = christmas
   │       └── christmas.css
   │
   └── theme = july4
           └── july4.css
```

The goal is to keep **business logic separate from presentation**.

---

# Roadmap

## Phase 1 — Foundation

* [x] Create Spring Boot application
* [x] Java 21
* [x] Maven build
* [x] Git repository
* [x] GitHub repository
* [x] Linux/LXC deployment
* [x] `pip.local` via mDNS
* [x] Port 80 web server
* [x] Fallout-inspired CRT interface
* [x] DATA / RADIO / QUEST navigation
* [x] JSON-based party profile
* [x] Menu REST API

## Phase 2 — Theme System

* [ ] Formalize theme/profile architecture
* [ ] Fallout theme
* [ ] Thanksgiving theme
* [ ] Christmas theme
* [ ] July 4th theme
* [ ] Theme-specific CSS
* [ ] CSS snow
* [ ] CSS fireworks
* [ ] Seasonal visual effects
* [ ] Runtime theme/profile selection

## Phase 3 — Party Menu

* [ ] Expand menu model
* [ ] Menu administration
* [ ] Ghoul menu assistant
* [ ] Genre/theme-aware menu generation
* [ ] Validate generated menu items
* [ ] Party-specific menu editing

## Phase 4 — Music

* [ ] Radio abstraction
* [ ] Now-playing API
* [ ] Play/pause
* [ ] Next/previous
* [ ] Volume control
* [ ] Home Assistant integration
* [ ] Music Assistant integration
* [ ] Theme-specific stations/playlists

## Phase 5 — Photos

* [ ] Photo API
* [ ] Slideshow
* [ ] Theme-specific transitions
* [ ] Photo source configuration
* [ ] Party photo display

## Phase 6 — Party Automation

* [ ] Party announcements
* [ ] Quest/objective system
* [ ] Automatic party modes
* [ ] Scheduled events
* [ ] Guest interaction
* [ ] Home Assistant automation
* [ ] Dedicated touchscreen controller

## Phase 7 — Deployment

* [ ] Docker image
* [ ] Production configuration
* [ ] Automated deployment
* [ ] Health endpoint
* [ ] Logging
* [ ] Monitoring
* [ ] Automated tests
* [ ] CI/CD

---

# Technology

Current and planned technologies include:

* Java 21
* Spring Boot
* Maven
* REST APIs
* HTML
* CSS
* JavaScript
* JSON
* Home Assistant
* Music Assistant
* Linux
* LXC
* Git
* GitHub
* mDNS / Avahi
* Docker
* Automated testing
* CI/CD

---

# Why Build This?

This project started with a simple idea:

> **What if the party's TV was part of the party?**

Instead of displaying a normal slideshow or static menu, the TV becomes an interactive themed console.

The project is intentionally practical and fun, but it also provides an opportunity to explore real software engineering concepts:

* application architecture
* configuration-driven design
* REST APIs
* dependency injection
* integration with external systems
* frontend/backend separation
* reusable themes
* automated testing
* deployment
* observability

Most importantly, this is software that actually gets used.

The first production test is a real party.

---

# Project Status

🚧 **Active development**

The first Fallout-themed deployment is currently being built.

☢️ **Welcome to the Wasteland.**

The next themes will prove that the system is bigger than Fallout.

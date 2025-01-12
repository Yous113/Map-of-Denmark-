# Map of Denmark: Visualization, Navigation, and Route Planning

## Project Overview

This project, developed during my second semester at the IT University of Copenhagen, is part of the **First-Year Project** course for the Bachelor in Software Development. It involves building a system to visualize and interact with OpenStreetMap (.OSM) data, allowing users to navigate, search, and plan routes on a map.

## Features

- Load and visualize road networks from .OSM files.
- Display different types of roads in distinct colors.
- Search for addresses and display results on the map.
- Plan routes between two points, supporting car and bike/walk modes:
  - **Car Mode**: Considers speed limits and average speeds.
  - **Bike Mode**: Avoids highways and focuses on the shortest path.
- Zoom and pan functionality with graphical scale indicators.

## Additional Features

- Adjustable GUI that adapts to window resizing.
- Fast binary file loading for quick startup.
- Interactive features such as route customization and intermediate stops (optional extensions).

## Technologies and Concepts

- **Java**: Core implementation using JavaFX for GUI.
- **Algorithms**: Efficient routing using A* algorithm.
- **Spatial Data Structures**: Optimized data representation for fast map rendering and querying, using KDTree.

## Getting Started

### Prerequisites

- **Java 11** or higher.
- **Gradle** for building and running the project.

### Running the Application

1. Clone the repository.
2. Build the project:
```bash
gradle build
```
3. Run the project 
```bash
gradle run
```

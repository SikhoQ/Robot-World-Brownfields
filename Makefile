VERSION=4.0.0
DEV_VERSION=4.0.0

# Targets
.PHONY: all compile test version release package clean build

# Default target
all: clean compile test version release package

# Compile the code
compile:
	mvn clean install compile

# Run all tests
test:
	mvn test

# Show the current version
version:
	@echo "Current version: $(VERSION)"

# Prepare for a release build
release:
	@echo "Preparing for release build..."
	sed -i 's/$(VERSION)/$(DEV_VERSION)/' pom.xml
	mvn clean install
	sed -i 's/$(DEV_VERSION)/$(VERSION)/' pom.xml
	@echo "Release build complete."

# Package the software
package: release
	@echo "Packaging the software..."
	mvn package
	@echo "Software packaged successfully."

# Clean the project
clean:
	mvn clean

# Build command with type parameter
build:
ifeq ($(type),release)
	$(MAKE) release
else
	$(MAKE) version
endif

VERSION=0.1.0
DEV_VERSION=1.0-SNAPSHOT

# Targets
.PHONY: all compile test-reference test-own version release package clean

# Default target
all: clean compile test version release package

# Compile the code
compile:
	mvn compile

# Run tests
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
build: compile
ifeq ($(type),release)
	$(MAKE) release
else
	$(MAKE) version
endif

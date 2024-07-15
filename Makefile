# Makefile for RobotWorld project

# Maven executable
MAVEN = mvn

# Version handling
VERSION ?= $(shell $(MAVEN) help:evaluate -Dexpression=project.version -q -DforceStdout)

# Targets
.PHONY: all clean compile test reference-test server-test package release tag-git

# Default target
all: compile test

# Clean target
clean:
	$(MAVEN) clean

# Compile target
compile:
	$(MAVEN) compile

# Test target (run all tests)
test: reference-test server-test

# Reference server test target
reference-test:
	@echo "Running tests against reference server..."
	$(MAVEN) test -Dtest.server=reference

# Server test target
server-test:
	@echo "Running tests against your own server..."
	$(MAVEN) test -Dtest.server=own

# Package target
package:
	$(MAVEN) package

# Release target (removes SNAPSHOT from version)
release:
	@echo "Preparing release version..."
	$(MAVEN) versions:set -DnewVersion=$(VERSION:SNAPSHOT=)

# Tag Git target
tag-git:
	@echo "Tagging version $(VERSION) in Git..."
	git tag release-$(VERSION)
	git push origin release-$(VERSION)

# Help target
help:
	@echo "RobotWorld Makefile Targets:"
	@echo "  make all         : Compile and run tests"
	@echo "  make clean       : Clean the project"
	@echo "  make compile     : Compile the project"
	@echo "  make test        : Run all tests"
	@echo "  make package     : Package the software"
	@echo "  make release     : Prepare a release (remove SNAPSHOT)"
	@echo "  make tag-git     : Tag the current version in Git"
	@echo "  make help        : Show this help message"

# Default target if no arguments are provided
.DEFAULT_GOAL := help

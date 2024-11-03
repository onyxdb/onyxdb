# onyxdb

## Workflows

We have 2 types of checks:
1. light-check (`light-check.yml`) - runs on any pr and push to master branch.
This check builds monorepo without re-generating codegen.
2. heavy-check (`heavy-check.yml`) - runs by cron task one time per day.
This check re-generates all available codegen and builds monorepo.

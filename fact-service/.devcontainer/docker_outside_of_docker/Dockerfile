# syntax=docker/dockerfile:1.4

FROM mcr.microsoft.com/devcontainers/base:bullseye

ENV container=docker

COPY --chmod=755 ./entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

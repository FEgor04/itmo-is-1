include .env

FRONTEND_IMAGE_NAME ?= is1-frontend
BACKEND_IMAGE_NAME ?= is1-backend

FRONTEND_VERSION ?= 0.1.1
BACKEND_VERSION ?= 0.1.2

REGISTRY_URI ?= registry

FRONTEND_IMAGE = ${REGISTRY_URI}/${FRONTEND_IMAGE_NAME}:${FRONTEND_VERSION}
BACKEND_IMAGE = ${REGISTRY_URI}/${BACKEND_IMAGE_NAME}:${BACKEND_VERSION}
POSTGRES_IMAGE=postgres
TRAEFIK_IMAGE=traefik:3.2

export

all: build-backend build-frontend

build-backend: backend
	cd backend && ./gradlew bootBuildImage --imageName=${BACKEND_IMAGE}

push-backend: 
	docker push ${BACKEND_IMAGE}

build-frontend: frontend
	cd frontend && docker build . -t ${FRONTEND_IMAGE}

push-frontend: 
	docker push ${FRONTEND_IMAGE}

dev-up:
	docker compose -f docker-compose.dev.yaml up

dev-down:
	docker compose -f docker-compose.dev.yaml down

prod-up:
	docker compose -f docker-compose.yaml up

prod-down:
	docker compose -f docker-compose.yaml down


BACKEND_IMAGE_NAME ?= is1-backend
FRONTEND_IMAGE_NAME ?= is1-backend

FRONTEND_VERSION ?= 0.1.0
BACKEND_VERSION ?= 0.1.0

REGISTRY_RUI ?= registry

build-backend:
	cd backend && ./gradlew bootBuildImage --imageName=${REGISTRY_URI}/${BACKEND_IMAGE_NAME}:${BACKEND_VERSION}

push-backend: build-backend
	docker push ${REGISTRY_URI}/${IMAGE_NAME}:${VERSION}

build-frontend:
	cd frontend && docker build . -t ${REGISTRY_URI}/${FRONTEND_IMAGE_NAME}:${FRONTEND_VERSION}

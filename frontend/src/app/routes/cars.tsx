import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/cars')({
  component: () => <div>Hello /cars!</div>,
})

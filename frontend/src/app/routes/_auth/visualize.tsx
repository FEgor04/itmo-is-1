import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/visualize')({
  component: () => <div>Hello /_auth/visualize!</div>,
})

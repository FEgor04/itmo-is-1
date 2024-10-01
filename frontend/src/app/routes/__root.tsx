import { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";

type RouterContext = {
  queryClient: QueryClient;
};

export const Route = createRootRouteWithContext<RouterContext>()({
  component: () => (
    <>
      <header className="border-b">
        <nav className="container mx-auto flex items-center h-14">
          <h1 className="text-2xl font-bold">Вебмакакинг</h1>
        </nav>
      </header>
      <main className="container mx-auto mt-8">
        <Outlet />
      </main>
      <TanStackRouterDevtools />
    </>
  ),
});

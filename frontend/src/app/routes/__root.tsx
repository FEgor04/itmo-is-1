import { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";
import webMonkeyIcon from "@/shared/ui/icon.png";

type RouterContext = {
  queryClient: QueryClient;
};

export const Route = createRootRouteWithContext<RouterContext>()({
  component: () => (
    <>
      <header className="border-b">
        <nav className="container mx-auto flex h-14 items-center">
          <h1 className="text-2xl font-bold inline-flex items-center space-x-2">
            <img src={webMonkeyIcon} className="size-8" />
            Вебмакакинг
          </h1>
        </nav>
      </header>
      <main className="container mx-auto mt-8">
        <Outlet />
      </main>
      <TanStackRouterDevtools />
    </>
  ),
});

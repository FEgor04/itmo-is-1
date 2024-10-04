import { QueryClient } from "@tanstack/react-query";
import {
  createRootRouteWithContext,
  Link,
  Outlet,
  useLocation,
} from "@tanstack/react-router";
import { TanStackRouterDevtools } from "@tanstack/router-devtools";
import webMonkeyIcon from "@/shared/ui/icon.png";

type RouterContext = {
  queryClient: QueryClient;
};

import { Car, PanelLeft, User } from "lucide-react";

import { Button } from "@/shared/ui/button";
import { Sheet, SheetContent, SheetTrigger } from "@/shared/ui/sheet";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/shared/ui/tooltip";
import { PrincipalProfile } from "@/entities/principal/profile";

function LocationTitle() {
  const location = useLocation();
  if (location.href.includes("/cars")) {
    return "Машины";
  }
  if (location.href.includes("/humans")) {
    return "Люди";
  }
}

export function Dashboard() {
  return (
    <div className="flex min-h-screen w-full flex-col bg-muted/40">
      <aside className="fixed inset-y-0 left-0 z-10 hidden w-14 flex-col border-r bg-background sm:flex">
        <nav className="flex flex-col items-center gap-4 px-2 sm:py-5">
          <Link
            to="/"
            className="group flex h-9 w-9 shrink-0 items-center justify-center gap-2 rounded-full border border-primary text-lg font-semibold text-primary-foreground md:h-8 md:w-8 md:text-base"
          >
            <img src={webMonkeyIcon} className="size-6 group-hover:scale-110" />
          </Link>
          <Tooltip>
            <TooltipTrigger asChild>
              <Link
                to="/humans"
                search={{
                  page: 1,
                  pageSize: 10,
                  sortBy: "id",
                  sortDirection: "asc",
                }}
                className="flex h-9 w-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:text-foreground data-[status=active]:bg-accent md:h-8 md:w-8"
              >
                <User className="h-5 w-5" />
              </Link>
            </TooltipTrigger>
            <TooltipContent side="right">Люди</TooltipContent>
          </Tooltip>
          <Tooltip>
            <TooltipTrigger asChild>
              <Link
                to="/cars"
                className="flex h-9 w-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:text-foreground data-[status=active]:bg-accent md:h-8 md:w-8"
              >
                <Car className="h-5 w-5" />
                <span className="sr-only">Машины</span>
              </Link>
            </TooltipTrigger>
            <TooltipContent side="right">Машины</TooltipContent>
          </Tooltip>
        </nav>
      </aside>
      <div className="flex flex-col sm:gap-4 sm:py-4 sm:pl-14">
        <header className="sticky top-0 z-30 flex h-14 items-center gap-4 border-b bg-background px-4 sm:static sm:h-auto sm:border-0 sm:bg-transparent sm:px-6">
          <Sheet>
            <SheetTrigger asChild>
              <Button size="icon" variant="outline" className="sm:hidden">
                <PanelLeft className="h-5 w-5" />
                <span className="sr-only">Toggle Menu</span>
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="sm:max-w-xs">
              <nav className="grid gap-6 text-lg font-medium">
                <Link
                  href="#"
                  className="group flex h-10 w-10 shrink-0 items-center justify-center gap-2 rounded-full border border-primary text-lg font-semibold text-primary-foreground md:text-base"
                >
                  <img src={webMonkeyIcon} className="size-6" />
                  <span className="sr-only">Вебмакакинг</span>
                </Link>
                <Link
                  to="/humans"
                  search={{
                    page: 1,
                    pageSize: 10,
                    sortBy: "id",
                    sortDirection: "asc",
                  }}
                  className="flex items-center gap-4 px-2.5 text-muted-foreground hover:text-foreground data-[state=active]:text-foreground"
                >
                  <User className="h-5 w-5" />
                  Люди
                </Link>
                <Link
                  to="/cars"
                  className="flex items-center gap-4 px-2.5 text-muted-foreground hover:text-foreground data-[state=active]:text-foreground"
                >
                  <Car className="h-5 w-5" />
                  Машины
                </Link>
              </nav>
            </SheetContent>
          </Sheet>
          <h1 className="text-2xl font-bold">
            <LocationTitle />
          </h1>
          <PrincipalProfile />
        </header>
        <main className="flex-1 items-start p-4 sm:px-6 sm:py-0">
          <Outlet />
          <TanStackRouterDevtools />
        </main>
      </div>
    </div>
  );
}

export const Route = createRootRouteWithContext<RouterContext>()({
  component: () => (
    <>
      <TooltipProvider>
        <Dashboard />
      </TooltipProvider>
    </>
  ),
});

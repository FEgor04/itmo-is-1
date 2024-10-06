import {
  focusManager,
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import { RouterProvider, createRouter } from "@tanstack/react-router";
import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import { routeTree } from "@/routeTree.gen";
import { AxiosError } from "axios";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        if (
          error instanceof AxiosError &&
          error.response &&
          400 <= error.response.status &&
          error.response.status < 500
        ) {
          return false;
        }
        return failureCount < 3;
      },
    },
  },
});

focusManager.setEventListener((handleFocus) => {
  // Listen to visibilitychange
  if (typeof window !== "undefined" && window.addEventListener) {
    const visibilitychangeHandler = () => {
      handleFocus();
    };
    window.addEventListener("focus", visibilitychangeHandler, false);
    return () => {
      // Be sure to unsubscribe if a new handler is set
      window.removeEventListener("focus", visibilitychangeHandler);
    };
  }
});

const router = createRouter({ routeTree, context: { queryClient } });

declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
      <ReactQueryDevtools client={queryClient} />
    </QueryClientProvider>
  </React.StrictMode>,
);

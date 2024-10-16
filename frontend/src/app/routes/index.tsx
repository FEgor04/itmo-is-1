import { getPrincipalQueryOptions } from "@/entities/principal/api";
import { Button } from "@/shared/ui/button";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute, Link } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  component: Page,
  loader: async ({context}) => {
    try { 
      const principal = await context.queryClient.ensureQueryData(getPrincipalQueryOptions())
      return principal
    }
    catch {
      // User is not authenticated. Swalling the exception
    }
  }
});

function Page() {
  return (
    <div className="space-y-8">
      <section className="space-y-4 text-center">
        <h1 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl lg:text-6xl/none">
          Крудошлепство
        </h1>
        <p>
          Добро пожаловать на очередную лабораторную работу на тему{" "}
          <span className="text-primary underline underline-offset-2">
            крудошлепство
          </span>
        </p>
      </section>
      <section className="mx-auto flex flex-col items-center justify-evenly gap-8 text-2xl">
        <ul className="list-inside list-disc space-y-4 text-muted-foreground">
          <li>Kotlin🔥 Spring Boot 🤢</li>
          <li>React🥰 TypeScript😍</li>
          <li>React Query😊 Tailwind CSS😇</li>
          <li>PostgreSQL😎 Jooq🐂💩</li>
        </ul>
        <CallToAction />
      </section>
    </div>
  );
}

function CallToAction() {
  const initialData = Route.useLoaderData()
  const { data } = useQuery({...getPrincipalQueryOptions(), initialData})

  if(data) {
    return <div>
    <Button asChild>
    <Link to="/humans" search={{page: 1, pageSize: 10}}>Вперед к круду!</Link>
    </Button>
    </div>
  }

  return <div className="flex gap-4">
          <Button asChild>
            <Link to="/signup">Регистрация</Link>
          </Button>
          <Button asChild variant="link">
            <Link to="/signin">Вход</Link>
          </Button>
        </div>
}

import { getPrincipalQueryOptions } from "@/entities/principal/api";
import { PaginatedQuerySchema } from "@/shared/pagination";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/shared/ui/card";
import { createFileRoute } from "@tanstack/react-router";

const SearchSchema = PaginatedQuerySchema;

export const Route = createFileRoute("/_auth/requests")({
  component: Page,
  validateSearch: SearchSchema,
  loader: async ({ context }) => {
    const me = await context.queryClient.ensureQueryData(
      getPrincipalQueryOptions(),
    );
    return me;
  },
});

function Page() {
  const me = Route.useLoaderData();
  if (me.role == "ADMIN") {
    return <AdminPage />;
  }
  return <AdminPage />
}

function AdminPage() {
  return (
    <div>
      <Card className="max-w-lg mx-auto bg-green-500 text-slate-50">
        <CardHeader>
          <CardTitle>🎉 Congratulations!</CardTitle>
        </CardHeader>
        <CardContent>
            Теперь вы можете посмотреть заявки других
            пользователей на становление администратором.

            Вы также можете редактировать и удалять объекты всех пользователей.
        </CardContent>
      </Card>
    </div>
  );
}

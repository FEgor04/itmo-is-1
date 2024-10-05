import {
  getAdminRequests,
  getPrincipalQueryOptions,
  useSendAdminRequestMutation,
} from "@/entities/principal/api";
import { useRequestsTable } from "@/entities/principal/requests-table";
import { PaginatedQuerySchema } from "@/shared/pagination";
import { Button } from "@/shared/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/shared/ui/card";
import { DataTable } from "@/shared/ui/data-table";
import { PaginationFooter } from "@/shared/ui/pagination";
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { z } from "zod";

const SearchSchema = PaginatedQuerySchema;

export const Route = createFileRoute("/_auth/requests")({
  component: Page,
  validateSearch: SearchSchema,
  loaderDeps: ({ search }) => search,
  loader: async ({ context, deps }) => {
    const me = context.queryClient.ensureQueryData(getPrincipalQueryOptions());
    const data = context.queryClient.ensureQueryData(getAdminRequests(deps));
    return Promise.all([me, data]);
  },
});

function Page() {
  const [me] = Route.useLoaderData();
  if (me.role == "ADMIN") {
    return <AdminPage />;
  }
  return <UserPage />;
}

function AdminPage() {
  const [_, initialData] = Route.useLoaderData();
  const search = Route.useSearch();
  const { data } = useQuery({ ...getAdminRequests(search), initialData });
  const table = useRequestsTable(data.values);
  const navigate = Route.useNavigate();
  function setQuery(
    updater: (
      previous: z.infer<typeof SearchSchema>,
    ) => z.infer<typeof SearchSchema>,
  ) {
    void navigate({
      search: updater,
    });
  }

  return (
    <div className="space-y-8">
      <Card className="mx-auto max-w-lg bg-green-500 text-slate-50">
        <CardHeader>
          <CardTitle>🎉 Congratulations!</CardTitle>
        </CardHeader>
        <CardContent>
          Теперь вы можете посмотреть заявки других пользователей на становление
          администратором. Вы также можете редактировать и удалять объекты всех
          пользователей.
        </CardContent>
      </Card>
      <main className="max-w-lg mx-auto space-y-4">
        <DataTable table={table} />
        <PaginationFooter
          query={search}
          setQuery={setQuery}
          total={data.total}
        />
      </main>
    </div>
  );
}

function UserPage() {
  const [initialData] = Route.useLoaderData();
  const { data: me } = useQuery({ ...getPrincipalQueryOptions(), initialData });
  if (me.adminRequestStatus == "NO_REQUEST") {
    return <NoRequestCard />;
  }
  if (me.adminRequestStatus == "PENDING") {
    return <PendingCard />;
  }
}

function NoRequestCard() {
  const { mutate, isPending } = useSendAdminRequestMutation();
  return (
    <div>
      <Card className="mx-auto max-w-lg">
        <CardHeader>
          <CardTitle>Администрирование</CardTitle>
        </CardHeader>
        <CardContent>
          Вы можете отправить заявку на становление администратором. После
          становлением администратором вы можете посмотреть заявки других
          пользователей на становление администратором, также вы сможете
          редактировать и удалять объекты всех пользователей.
        </CardContent>
        <CardFooter>
          <Button onClick={() => mutate()} disabled={isPending}>
            Отправить
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}

function PendingCard() {
  return (
    <div>
      <Card className="mx-auto max-w-lg bg-yellow-300">
        <CardHeader>
          <CardTitle>Администрирование</CardTitle>
        </CardHeader>
        <CardContent>
          Вы уже отправили заявку на становление администратором. Сейчас она
          находится на рассмотрении
        </CardContent>
      </Card>
    </div>
  );
}

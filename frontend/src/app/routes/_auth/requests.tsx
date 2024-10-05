import {
  getPrincipalQueryOptions,
  useSendAdminRequestMutation,
} from "@/entities/principal/api";
import { PaginatedQuerySchema } from "@/shared/pagination";
import { Button } from "@/shared/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/shared/ui/card";
import { useQuery } from "@tanstack/react-query";
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
  return <UserPage />;
}

function AdminPage() {
  return (
    <div>
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
    </div>
  );
}

function UserPage() {
  const initialData = Route.useLoaderData();
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

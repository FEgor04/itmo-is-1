import { Button } from "@/shared/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { useQuery } from "@tanstack/react-query";
import { getPrincipalQueryOptions } from "./api";
import { Link, useLocation, useNavigate } from "@tanstack/react-router";
import { Skeleton } from "@/shared/ui/skeleton";
import { useSignOutMutation } from "@/shared/auth";

export function PrincipalProfile() {
  const { data, isError } = useQuery(getPrincipalQueryOptions());
  const { mutate, isPending } = useSignOutMutation();
  const location = useLocation();
  const navigate = useNavigate();

  const noButtonLocations = ["/signin", "/signup"];
  if (isError && !noButtonLocations.includes(location.pathname)) {
    return (
      <Button asChild>
        <Link to="/signin">Войти</Link>
      </Button>
    );
  } else if (isError && noButtonLocations.includes(location.pathname)) {
    return <></>;
  }

  if (data == undefined) {
    return <Skeleton className="size-8 rounded-full" />;
  }

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="outline"
          size="icon"
          className="ml-auto overflow-hidden rounded-full"
        >
          {data.username[0]}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end">
        <DropdownMenuLabel>{data.username}</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          onClick={() =>
            navigate({
              to: "/requests",
              search: {
                page: 1,
                pageSize: 10,
              },
            })
          }
        >
          Заявки
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          disabled={isPending}
          onClick={() =>
            mutate(void 0, { onSuccess: () => navigate({ to: "/signin" }) })
          }
        >
          Выйти
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

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
import { Link } from "@tanstack/react-router";
import { Skeleton } from "@/shared/ui/skeleton";
import { useSignOutMutation } from "@/shared/auth";

export function PrincipalProfile() {
  const { data, isError } = useQuery(getPrincipalQueryOptions());
  const { mutate, isPending } = useSignOutMutation();

  if (isError) {
    return (
      <Button asChild>
        <Link to="/signin">Войти</Link>
      </Button>
    );
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
        <DropdownMenuItem>Заявки</DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem disabled={isPending} onClick={() => mutate()}>
          Выйти
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

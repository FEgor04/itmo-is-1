import {
  ColumnDef,
  getCoreRowModel,
  OnChangeFn,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { Car } from "./model";
import { Button } from "@/shared/ui/button";
import { Ellipsis } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { useState } from "react";
import { EditCarDialogContent } from "./edit";
import { Dialog } from "@/shared/ui/dialog";
import { useDeleteCarMutation } from "./api";
import { getPrincipalQueryOptions } from "../principal/api";
import { useQuery } from "@tanstack/react-query";

const carColumns: Array<ColumnDef<Car>> = [
  {
    accessorKey: "id",
    header: "ID",
  },
  {
    accessorKey: "brand",
    header: "Брэнд",
  },
  {
    accessorKey: "model",
    header: "Модель",
  },
  {
    accessorKey: "color",
    header: "Цвет",
  },
  {
    accessorKey: "cool",
    header: "Крутая",
  },
  {
    id: "actions",
    header: "",
    cell: ({ row }) => <Actions car={row.original} />,
  },
];

function Actions({ car }: { car: Car }) {
  const [open, setEditOpen] = useState(false);
  const { data: me } = useQuery(getPrincipalQueryOptions());
  const canEdit = car.ownerId === me?.id || me?.role == "ADMIN";
  const { mutate, isPending } = useDeleteCarMutation();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant="ghost"
          className="flex h-8 w-8 p-0 data-[state=open]:bg-muted"
        >
          <Ellipsis className="h-4 w-4" />
          <span className="sr-only">Open menu</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="end" className="w-[160px]">
        <DropdownMenuItem disabled={!canEdit} onClick={() => setEditOpen(true)}>
          Редактировать
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          disabled={isPending || !canEdit}
          onClick={() => mutate(car.id)}
        >
          Удалить
        </DropdownMenuItem>
      </DropdownMenuContent>
      <Dialog open={open} onOpenChange={setEditOpen}>
        <EditCarDialogContent car={car} onClose={() => setEditOpen(false)} />
      </Dialog>
    </DropdownMenu>
  );
}

export function useCarTable(
  data: Array<Car>,
  sortingState: SortingState,
  handleSetSortingState: OnChangeFn<SortingState>,
) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns: carColumns,
    state: {
      sorting: sortingState,
    },
    onSortingChange: handleSetSortingState,
    defaultColumn: {
      cell: ({ getValue }) => {
        const value = getValue();
        if (value === undefined) {
          return <>&mdash;</>;
        }
        if (typeof value === "boolean") {
          return value ? "Да" : "Нет";
        }
        return value;
      },
    },
  });
}

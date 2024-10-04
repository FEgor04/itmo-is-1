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

function Actions({}: { car: Car }) {
  const [_, setEditOpen] = useState(false);
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
        <DropdownMenuItem onClick={() => setEditOpen(true)}>
          Редактировать
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem>Удалить</DropdownMenuItem>
      </DropdownMenuContent>
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

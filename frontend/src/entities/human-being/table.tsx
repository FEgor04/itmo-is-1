import {
  ColumnDef,
  getCoreRowModel,
  OnChangeFn,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { FetchedHumanBeing } from "./model";
import { MoodBadge } from "../enums/mood";
import { WeaponTypeBadge } from "../enums/weapon-type";
import { Button } from "@/shared/ui/button";
import { Ellipsis, Trash } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { useDeleteHumanBeingMutation } from "./api";

const HumanBeingTableDef: Array<ColumnDef<FetchedHumanBeing>> = [
  {
    accessorKey: "id",
    header: "ID",
  },
  {
    accessorKey: "name",
    header: "Имя",
  },
  {
    accessorKey: "coordinates.x",
    header: "X",
  },
  {
    accessorKey: "coordinates.y",
    header: "Y",
  },
  {
    accessorKey: "creationDate",
    header: "Дата создания",
    cell: ({ row }) => {
      return row.original.creationDate.toLocaleDateString();
    },
  },
  {
    accessorKey: "realHero",
    header: "Real Hero?",
  },
  {
    accessorKey: "hasToothpick",
    header: "Есть зубочистка?",
  },
  {
    accessorKey: "mood",
    header: "Настроение",
    cell: ({ row }) => {
      return <MoodBadge value={row.original.mood} />;
    },
  },
  {
    accessorKey: "impactSpeed",
    header: "Скорость",
  },
  {
    accessorKey: "weaponType",
    header: "Тип оружия",
    cell: ({ row }) => {
      return <WeaponTypeBadge value={row.original.weaponType} />;
    },
  },
  {
    id: "actions",
    header: "Действия",
    cell: ({ row }) => {
      const { mutate, isPending } = useDeleteHumanBeingMutation();
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
            <DropdownMenuItem disabled={isPending}>
              Редактировать
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem
              disabled={isPending}
              onClick={() => mutate(row.original.id)}
            >
              Удалить
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      );
    },
  },
];

export function useHumanBeingTable(
  data: Array<FetchedHumanBeing>,
  sortingState: SortingState,
  handleSetSortingState: OnChangeFn<SortingState>,
) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns: HumanBeingTableDef,
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

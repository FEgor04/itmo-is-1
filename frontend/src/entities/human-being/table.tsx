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
import { Ellipsis } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { useDeleteHumanBeingMutation } from "./api";
import { useState } from "react";
import { Dialog } from "@/shared/ui/dialog";
import { EditHumanBeingDialogContent } from "./ui/edit";
import { useQuery } from "@tanstack/react-query";
import { getPrincipalQueryOptions } from "../principal/api";

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
    accessorKey: "car.brand",
    header: "Модель автомобиля",
  },
  {
    accessorKey: "car.model",
    header: "Брэнд автомобиля",
  },
  {
    accessorKey: "car.color",
    header: "Цвет автомобиля",
  },
  {
    accessorKey: "car.cool",
    header: "Крутая?",
  },
  {
    id: "actions",
    header: "",
    cell: ({ row }) => <Actions humanBeing={row.original} />,
  },
];

function Actions({ humanBeing }: { humanBeing: FetchedHumanBeing }) {
  const [isEditOpen, setEditOpen] = useState(false);
  const { data: me } = useQuery(getPrincipalQueryOptions());
  const { mutate, isPending } = useDeleteHumanBeingMutation();
  const canEdit = humanBeing.ownerId === me?.id || me?.role == "ADMIN";
  return (
    <>
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
          <DropdownMenuItem
            disabled={!canEdit}
            onClick={() => setEditOpen(true)}
          >
            Редактировать
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem
            disabled={isPending || !canEdit}
            onClick={() => mutate(humanBeing.id)}
          >
            Удалить
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
      <Dialog open={isEditOpen} onOpenChange={setEditOpen}>
        <EditHumanBeingDialogContent
          humanBeing={humanBeing}
          onClose={() => setEditOpen(false)}
        />
      </Dialog>
    </>
  );
}

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

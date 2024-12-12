import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Import } from "@/entities/import";
import { Button } from "@/shared/ui/button";
import { ImportStatusBadge } from "@/entities/import/ui/status.tsx";
import { Download } from "lucide-react";

export const columns: Array<ColumnDef<Import>> = [
  {
    accessorKey: "id",
    header: "ID",
  },
  {
    accessorKey: "status",
    header: "Статус",
    cell: ({ row }) => <ImportStatusBadge status={row.original.status} />,
  },
  {
    accessorKey: "author.username",
    header: "Пользователь",
  },
  {
    accessorKey: "createdEntities",
    header: () => <div className="text-right">Кол-во добавленых сущностей</div>,
    cell: ({ row: { original } }) => (
      <div className="text-right">
        {original.status == "finished" ? (
          original.createdEntities
        ) : (
          <>&mdash;</>
        )}
      </div>
    ),
  },
  {
    accessorKey: "startedAt",
    header: "Дата начала",
  },
  {
    accessorKey: "finishedAt",
    header: "Дата конца",
  },
  {
    id: "actions",
    cell: ({row}) => {
      if(row.original.status != "finished") return

      return <Button variant="outline" size="icon" className="size-8" asChild>
        <a href={`${import.meta.env.VITE_MINIO_URL}/lab1/${row.original.id}.csv`} target="_blank">
          <Download className="size-4" />
        </a>
      </Button>
    }
  }
];

export function useImportsTable(data: Array<Import>) {
  return useReactTable({
    getCoreRowModel: getCoreRowModel(),
    data,
    columns,
    enableSorting: false,
    defaultColumn: {
      cell: ({ getValue }) => {
        const value = getValue();
        if (value === undefined) {
          return <>&mdash;</>;
        }
        if (typeof value === "boolean") {
          return value ? "Да" : "Нет";
        }
        if (value instanceof Date) {
          return value.toLocaleString();
        }
        return value;
      },
    },
  });
}

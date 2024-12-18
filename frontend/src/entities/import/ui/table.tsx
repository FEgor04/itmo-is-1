import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";
import { Import } from "@/entities/import";
import { Button } from "@/shared/ui/button";
import { ImportStatusBadge } from "@/entities/import/ui/status.tsx";
import { Download } from "lucide-react";
import { ApiInstance } from "@/shared/instance.ts";

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

      function fetchFile() {
        ApiInstance.api.getImportFile(row.original.id, {format: "blob"}).then((res) => {
          const blob = res.data as unknown as Blob
          const fileUrl = window.URL.createObjectURL(blob)
          const alink = document.createElement("a")
          alink.href = fileUrl
          alink.download = `${row.original.id}.csv`
          alink.click()
        })
      }

      return <Button variant="outline" size="icon" className="size-8" onClick={fetchFile}>
          <Download className="size-4" />
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

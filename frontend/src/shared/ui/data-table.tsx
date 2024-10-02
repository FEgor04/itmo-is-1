"use client";

import { flexRender, Header, Table as RTable } from "@tanstack/react-table";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/shared/ui/table";
import { Button } from "./button";
import {
  ArrowDown,
  ArrowUp,
  ChevronsUpDown,
} from "lucide-react";

interface DataTableProps<TData> {
  table: RTable<TData>;
}

export function DataTable<TData>({ table }: DataTableProps<TData>) {
  return (
    <div className="rounded-md border">
      <Table>
        <TableHeader>
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => (
                <TableColumnHeader header={header} />
              ))}
            </TableRow>
          ))}
        </TableHeader>
        <TableBody>
          {table.getRowModel().rows?.length ? (
            table.getRowModel().rows.map((row) => (
              <TableRow
                key={row.id}
                data-state={row.getIsSelected() && "selected"}
              >
                {row.getVisibleCells().map((cell) => (
                  <TableCell key={cell.id}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </TableCell>
                ))}
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell
                colSpan={table
                  .getHeaderGroups()
                  .map((it) => it.headers.length)
                  .reduce((a, b) => a + b, 0)}
                className="h-24 text-center"
              >
                Нет результатов
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
    </div>
  );
}

function TableColumnHeader<T>({ header }: { header: Header<T, unknown> }) {
  const sortable = header.column.getCanSort();
  if (!sortable) {
    return (
      <TableHead key={header.id}>
        {header.isPlaceholder
          ? null
          : flexRender(header.column.columnDef.header, header.getContext())}
      </TableHead>
    );
  }

  const sorted = header.column.getIsSorted();

  function onClick() {
    if (sorted === false) {
      header.column.toggleSorting(false);
    } else if (sorted == "asc") {
      header.column.toggleSorting(true);
    } else if (sorted == "desc") {
      header.column.toggleSorting();
    }
  }

  return (
    <TableHead key={header.id}>
      <Button className="px-1 py-0" variant="ghost" onClick={onClick}>
        {header.isPlaceholder
          ? null
          : flexRender(header.column.columnDef.header, header.getContext())}
        {sorted == false && <ChevronsUpDown className="ml-2 size-4" />}
        {sorted == "asc" && <ArrowUp className="ml-2 size-4" />}
        {sorted == "desc" && <ArrowDown className="ml-2 size-4" />}
      </Button>
    </TableHead>
  );
}

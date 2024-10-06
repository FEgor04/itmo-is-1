import {
  ChevronLeft,
  ChevronRight,
  ChevronsLeft,
  ChevronsRight,
} from "lucide-react";
import { ALLOWED_PAGE_SIZE } from "../pagination";
import { Button } from "./button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./select";

type Props<T> = {
  query: T;
  total: number;
  setQuery: (updater: (prev: T) => T) => void;
};

export function PaginationFooter<T extends { page: number; pageSize: number }>({
  query,
  setQuery,
  total,
}: Props<T>) {
  const totalPages = Math.ceil(total / query.pageSize);
  const hasPrevious = query.page > 1;
  const hasNext = query.page < totalPages;
  return (
    <footer className="flex w-full justify-between">
      <div className="flex items-center space-x-2">
        <p className="text-sm font-medium">Строк на странице</p>
        <Select
          value={query.pageSize.toString()}
          onValueChange={(pageSize) =>
            setQuery((prev) => ({
              ...prev,
              pageSize: Number(pageSize),
            }))
          }
        >
          <SelectTrigger className="h-8 w-auto">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            {ALLOWED_PAGE_SIZE.map((pageSize) => (
              <SelectItem value={String(pageSize)} key={pageSize}>
                {pageSize}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>
      <nav className="space-x-1 md:space-x-2">
        <Button
          variant="outline"
          className="size-8 p-0"
          disabled={!hasPrevious}
          onClick={() =>
            setQuery((prev) => ({
              ...prev,
              page: 1,
            }))
          }
        >
          <ChevronsLeft className="size-4" />
        </Button>
        <Button
          variant="outline"
          className="size-8 p-0"
          disabled={!hasPrevious}
          onClick={() =>
            setQuery((prev) => ({
              ...prev,
              page: prev.page - 1,
            }))
          }
        >
          <ChevronLeft className="size-4" />
        </Button>
        <Button
          variant="outline"
          className="size-8 p-0"
          disabled={!hasNext}
          onClick={() =>
            setQuery((prev) => ({
              ...prev,
              page: prev.page + 1,
            }))
          }
        >
          <ChevronRight className="size-4" />
        </Button>
        <Button
          variant="outline"
          className="size-8 p-0"
          disabled={!hasNext}
          onClick={() =>
            setQuery((prev) => ({
              ...prev,
              page: totalPages,
            }))
          }
        >
          <ChevronsRight className="size-4" />
        </Button>
      </nav>
    </footer>
  );
}

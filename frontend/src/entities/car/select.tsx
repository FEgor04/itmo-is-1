import React, { useState } from "react";
import { Popover, PopoverContent, PopoverTrigger } from "@/shared/ui/popover";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
  CommandLoading,
} from "@/shared/ui/command";
import { cn } from "@/shared/lib";
import { Button } from "@/shared/ui/button";
import { ChevronsUpDown, Check } from "lucide-react";
import { z } from "zod";
import { BaseCarSchema } from "./model";
import { useQuery } from "@tanstack/react-query";
import { getCarsQueryOptions } from "./api";
import { useDebouncedCallback } from "use-debounce";

type CarID = z.infer<typeof BaseCarSchema>["id"];

type Props = {
  value: CarID | undefined;
  onChange: (value: CarID | undefined) => void;
  className?: string;
} & React.ComponentProps<typeof Button>;

export function SelectCar({ value, onChange, ...props }: Props) {
  const [open, setOpen] = React.useState(false);
  const [input, setInput] = useState("");
  const { data, isLoading } = useQuery(
    getCarsQueryOptions({ page: 1, pageSize: 10, model: input }),
  );
  const options = data?.values;
  const setInputDebounced = useDebouncedCallback(setInput, 500);

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          {...props}
        >
          {value
            ? options?.find((framework) => framework.id === value)?.model
            : "Выберите автомобиль..."}
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[200px] p-0">
        <Command shouldFilter={false}>
          <CommandInput
            defaultValue={input}
            onValueChange={(e) => setInputDebounced(e)}
            placeholder="Поиск"
          />
          <CommandList>
            {isLoading && <CommandLoading>Загрузка...</CommandLoading>}
            {!isLoading && <CommandEmpty>Не найдено.</CommandEmpty>}
            <CommandGroup>
              {(options ?? []).map((option) => (
                <CommandItem
                  key={option.id}
                  value={String(option.id)}
                  onSelect={() => {
                    onChange(option.id === value ? undefined : option.id);
                    setOpen(false);
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value === option.id ? "opacity-100" : "opacity-0",
                    )}
                  />
                  {option.model}
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
}

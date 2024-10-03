import React from "react";
import { Popover, PopoverContent, PopoverTrigger } from "@/shared/ui/popover";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/shared/ui/command";
import { cn } from "@/shared/lib";
import { Button } from "@/shared/ui/button";
import { ChevronsUpDown, Check } from "lucide-react";
import { z } from "zod";
import { BaseCarSchema } from "./model";

type CarID = z.infer<typeof BaseCarSchema>["id"];

type Props = {
  value: CarID | undefined;
  onChange: (value: CarID | undefined) => void;
  className?: string;
};

export function SelectCar({ value, onChange, ...props }: Props) {
  const [open, setOpen] = React.useState(false);
  const options: Array<z.infer<typeof BaseCarSchema>> = [
    {
      id: 1,
      brand: "Lada",
      model: "Kalina",
      color: "red",
      cool: true,
    },
  ];

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
            ? options.find((framework) => framework.id === value)?.model
            : "Выберите автомобиль..."}
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[200px] p-0">
        <Command>
          <CommandInput placeholder="Поиск" />
          <CommandList>
            <CommandEmpty>No framework found.</CommandEmpty>
            <CommandGroup>
              {options.map((option) => (
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

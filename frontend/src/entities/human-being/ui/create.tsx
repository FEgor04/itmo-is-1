import {
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/shared/ui/dialog";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { CreateHumanBeingSchema } from "../api";
import { z } from "zod";
import { Form, FormField, FormItem, FormMessage } from "@/shared/ui/form";
import { Label } from "@/shared/ui/label";
import { Input } from "@/shared/ui/input";
import { Button } from "@/shared/ui/button";
import { Checkbox } from "@/shared/ui/checkbox";
import { SelectMood } from "@/entities/enums/mood";
import { SelectWeaponType } from "@/entities/enums/weapon-type";

export function CreateHumanBeingDialogContent() {
  const form = useForm<z.infer<typeof CreateHumanBeingSchema>>({
    resolver: zodResolver(CreateHumanBeingSchema),
    defaultValues: {
      hasToothpick: false,
      realHero: false,
    },
  });

  function onSubmit(values: z.infer<typeof CreateHumanBeingSchema>) {
    console.log(values);
  }

  return (
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Создание Human Being</DialogTitle>
      </DialogHeader>
      <Form {...form}>
        <form
          id="create-human-being-form"
          onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}
          className="space-y-4"
        >
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <Label>Имя</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="coordinates.x"
            render={({ field }) => (
              <FormItem>
                <Label>X</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="coordinates.y"
            render={({ field }) => (
              <FormItem>
                <Label>Y</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="realHero"
            render={({ field: { value, onChange, ...field } }) => (
              <FormItem className="flex items-center space-x-3 space-y-0">
                <Checkbox
                  checked={value}
                  onCheckedChange={(value) => onChange(value)}
                  {...field}
                />
                <Label>Настоящий герой?</Label>
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="hasToothpick"
            render={({ field: { value, onChange, ...field } }) => (
              <FormItem className="flex items-center space-x-3 space-y-0">
                <Checkbox
                  checked={value}
                  onCheckedChange={(value) => onChange(value)}
                  {...field}
                />
                <Label>Зубочистка есть?</Label>
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="mood"
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            render={({ field: { ref: _, ...field } }) => (
              <FormItem>
                <Label>Настроение</Label>
                <SelectMood {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="impactSpeed"
            render={({ field }) => (
              <FormItem>
                <Label>Скорость</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="weaponType"
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            render={({ field: { ref: _, ...field } }) => (
              <FormItem>
                <Label>Тип оружия</Label>
                <SelectWeaponType {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
        </form>
      </Form>
      <DialogFooter>
        <Button type="submit" form="create-human-being-form">
          Создать
        </Button>
      </DialogFooter>
    </DialogContent>
  );
}

import {
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/shared/ui/dialog";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { EditHumanBeingSchema, useEditHumanBeingMutation } from "../api";
import { z } from "zod";
import { Form, FormField, FormItem, FormMessage } from "@/shared/ui/form";
import { Label } from "@/shared/ui/label";
import { Input } from "@/shared/ui/input";
import { Button } from "@/shared/ui/button";
import { Checkbox } from "@/shared/ui/checkbox";
import { SelectMood } from "@/entities/enums/mood";
import { SelectWeaponType } from "@/entities/enums/weapon-type";
import { SelectCar } from "@/entities/car/select";
import { FetchedHumanBeing } from "../model";

type Props = {
  humanBeing: FetchedHumanBeing;
  onClose: () => void;
};

export function EditHumanBeingDialogContent({ humanBeing, onClose }: Props) {
  const form = useForm<z.infer<typeof EditHumanBeingSchema>>({
    resolver: zodResolver(EditHumanBeingSchema),
    defaultValues: {
      name: humanBeing.name,
      car: humanBeing.car.id,
      id: humanBeing.id,
      coordinates: humanBeing.coordinates,
      realHero: humanBeing.realHero,
      hasToothpick: humanBeing.hasToothpick,
      mood: humanBeing.mood,
      weaponType: humanBeing.weaponType,
      impactSpeed: humanBeing.impactSpeed,
    },
  });
  const { mutate, isPending } = useEditHumanBeingMutation();

  function onSubmit(values: z.infer<typeof EditHumanBeingSchema>) {
    mutate(values, {
      onSuccess: () => {
        onClose();
      },
    });
  }

  return (
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Изменение Human Being</DialogTitle>
      </DialogHeader>
      <Form {...form}>
        <form
          id="edit-human-being-form"
          onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}
          className="2 max-h-[75vh] space-y-4 overflow-y-auto"
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
                <Input required {...field} />
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
                <Input required {...field} />
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
                <Input required {...field} />
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
          <FormField
            control={form.control}
            name="car"
            render={({ field: { value, onChange } }) => (
              <FormItem>
                <Label>Машина</Label>
                <div>
                  <SelectCar
                    className="inline-flex w-full justify-between"
                    value={value}
                    onChange={onChange}
                  />
                </div>
                <FormMessage />
              </FormItem>
            )}
          />
        </form>
      </Form>
      <DialogFooter>
        <Button disabled={isPending} type="submit" form="edit-human-being-form">
          Сохранить
        </Button>
      </DialogFooter>
    </DialogContent>
  );
}

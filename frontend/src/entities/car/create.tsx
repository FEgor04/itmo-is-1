import {
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/shared/ui/dialog";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Form, FormField, FormItem, FormMessage } from "@/shared/ui/form";
import { Label } from "@/shared/ui/label";
import { Input } from "@/shared/ui/input";
import { Button } from "@/shared/ui/button";
import { CreateCarSchema, useCreateCarMutation } from "./api";
import { Checkbox } from "@/shared/ui/checkbox";

export function CreateCarDialogContent({ onClose }: { onClose: () => void }) {
  const form = useForm<z.infer<typeof CreateCarSchema>>({
    resolver: zodResolver(CreateCarSchema),
    defaultValues: {
      cool: false,
    },
  });
  const { mutate, isPending } = useCreateCarMutation();

  function onSubmit(values: z.infer<typeof CreateCarSchema>) {
    mutate(values, {
      onSuccess: onClose,
    });
  }

  return (
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Создание машины</DialogTitle>
      </DialogHeader>
      <Form {...form}>
        <form
          id="create-car-form"
          onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}
          className="space-y-4"
        >
          <FormField
            control={form.control}
            name="brand"
            render={({ field }) => (
              <FormItem>
                <Label>Брэнд</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="model"
            render={({ field }) => (
              <FormItem>
                <Label>Модель</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="color"
            render={({ field }) => (
              <FormItem>
                <Label>Цвет</Label>
                <Input {...field} />
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="cool"
            render={({ field: { value, onChange, ...field } }) => (
              <FormItem className="flex items-center space-x-3 space-y-0">
                <Checkbox
                  checked={value}
                  onCheckedChange={(value) => onChange(value)}
                  {...field}
                />
                <Label>Крутая?</Label>
              </FormItem>
            )}
          />
        </form>
      </Form>
      <DialogFooter>
        <Button disabled={isPending} type="submit" form="create-car-form">
          Создать
        </Button>
      </DialogFooter>
    </DialogContent>
  );
}

import { SignInSchema } from "@/shared/auth";
import { Button } from "@/shared/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/shared/ui/card";
import { FormField, FormItem } from "@/shared/ui/form";
import { Input } from "@/shared/ui/input";
import { Label } from "@/shared/ui/label";
import { zodResolver } from "@hookform/resolvers/zod";
import { createFileRoute } from "@tanstack/react-router";
import { Form, useForm } from "react-hook-form";
import { z } from "zod";

export const Route = createFileRoute("/signup")({
  component: Page,
});
const schema = SignInSchema;
type Values = z.infer<typeof schema>;

function Page() {
  const form = useForm<Values>({
    resolver: zodResolver(schema),
  });

  function onSubmit(data: Values) {
    console.log(data);
  }

  return (
    <div>
      <Card className="mx-auto mt-8 max-w-lg">
        <CardHeader>
          <CardTitle>Регистрация</CardTitle>
          <CardDescription>Регистрация нового пользователя</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form id="signup">
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <Label>Имя пользователя</Label>
                    <Input required {...field} />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <Label>Пароль</Label>
                    <Input type="password" required {...field} />
                  </FormItem>
                )}
              />
            </form>
          </Form>
        </CardContent>
        <CardFooter>
          <Button type="submit" form="signup">
            Отправить
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}

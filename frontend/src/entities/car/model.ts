import z from "zod";

export const CarIDSchema = z.number();

export const BaseCarSchema = z.object({
  id: CarIDSchema,
  brand: z.string(),
  model: z.string(),
  color: z.string(),
  cool: z.boolean(),
});

export const CarSchemaKeys = BaseCarSchema.keyof()
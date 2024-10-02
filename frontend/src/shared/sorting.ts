import { z } from "zod";

export function SortingQuerySchema<T extends z.ZodRawShape>(
  baseSchema: z.ZodObject<T>,
) {
  return z.object({
    sortBy: baseSchema.keyof().optional(),
    sortDirection: z.enum(["asc", "desc"]).optional(),
  });
}

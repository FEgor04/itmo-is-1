import { z } from "zod";

export function SortingQuerySchema<T extends [string, ...string[]]>(
  baseSchema: z.ZodEnum<T>,
) {
  return z.object({
    sortBy: baseSchema.optional(),
    sortDirection: z.enum(["asc", "desc"]).optional(),
  });
}

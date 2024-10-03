import z from "zod";
import { MoodSchema } from "../enums/mood";
import { WeaponTypeSchema } from "../enums/weapon-type";
import { BaseCarSchema } from "../car/model";
import {
  MUST_BE_INT_CONFIG,
  REQUIRED_CONFIG,
} from "@/shared/zod";

export const BaseHumanBeingSchema = z.object({
  id: z.number().gt(0),
  name: z.string(REQUIRED_CONFIG).min(1, REQUIRED_CONFIG),
  coordinates: z.object({
    x: z.coerce.number(MUST_BE_INT_CONFIG),
    y: z.coerce.number(MUST_BE_INT_CONFIG),
  }),
  creationDate: z.coerce.date(REQUIRED_CONFIG),
  realHero: z.boolean(),
  hasToothpick: z.boolean(),
  mood: MoodSchema,
  impactSpeed: z.coerce.number(MUST_BE_INT_CONFIG)
    .max(108, "Значение должно быть меньше 108")
    .optional(),
  weaponType: WeaponTypeSchema,
});

export const FetchedHumanBeingSchema = BaseHumanBeingSchema.extend({
  car: BaseCarSchema,
});
export const FetchedHumanBeingSchemaKeys = z.enum([
  "id",
  "name",
  "coordinates_x",
  "coordinates_y",
  "creationDate",
  "realHero",
  "hasToothpick",
  "mood",
  "impactSpeed",
  "weaponType",
]);

export type FetchedHumanBeing = z.infer<typeof FetchedHumanBeingSchema>;

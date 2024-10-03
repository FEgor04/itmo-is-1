import z from "zod";
import { MoodSchema } from "../enums/mood";
import { WeaponTypeSchema } from "../enums/weapon-type";
import { BaseCarSchema } from "../car/model";

const MUST_BE_INT_CONFIG = {
  message: "Значение должно быть числом",
};

export const BaseHumanBeingSchema = z.object({
  id: z.number().gt(0),
  name: z.string().min(1),
  coordinates: z.object({
    x: z.coerce.number(MUST_BE_INT_CONFIG).optional(),
    y: z.coerce.number(MUST_BE_INT_CONFIG),
  }),
  creationDate: z.coerce.date(),
  realHero: z.boolean(),
  hasToothpick: z.boolean(),
  mood: MoodSchema,
  impactSpeed: z.coerce.number(MUST_BE_INT_CONFIG).max(108, "Значение должно быть меньше 108").optional(),
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

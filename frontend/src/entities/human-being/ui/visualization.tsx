import {
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/shared/ui/chart";
import { FetchedHumanBeing } from "../model";
import { CartesianGrid, Scatter, ScatterChart, XAxis, YAxis } from "recharts";
import { useState } from "react";
import { Dialog } from "@/shared/ui/dialog";
import { EditHumanBeingDialogContent } from "./edit";

type Props = {
  humans: Array<FetchedHumanBeing>;
};

const stringToColour = (str: string) => {
  let hash = 0;
  str.split('').forEach(char => {
    hash = char.charCodeAt(0) + ((hash << 5) - hash)
  })
  let colour = '#'
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xff
    colour += value.toString(16).padStart(2, '0')
  }
  console.log(colour)
  return colour
}

export function HumansVisualization({ humans }: Props) {
  const [humanBeing, setHumanBeing] = useState<FetchedHumanBeing | undefined>()

  if(humans.length == 0) {
    return <h3 className="text-center text-2xl font-bold p-8">
    Нет данных
    </h3>
  }

  return (
    <>
    <ChartContainer config={{}} className="min-h-[400px] w-full mx-auto">
      <ScatterChart>
        <CartesianGrid />
        <XAxis dataKey="x"  type="number" tickLine={false} tickMargin={10} axisLine={false} />
        <YAxis dataKey="y" type="number" width={0}  tickLine={false} tickMargin={10} axisLine={false} />
        <ChartTooltip content={<ChartTooltipContent />} />
        {humans.map(it => <Scatter  data={[it.coordinates]} name={it.name} fill={stringToColour(String(it.ownerId * 51324))} key={it.id} onClick={() => {
          setHumanBeing(it)
        }}  />)}
      </ScatterChart>
    </ChartContainer>
      <Dialog open={!!humanBeing} onOpenChange={() => setHumanBeing(undefined)}>
      {humanBeing && <EditHumanBeingDialogContent humanBeing={humanBeing} onClose={() => setHumanBeing(undefined)} />}
      </Dialog>
    </>
  );
}

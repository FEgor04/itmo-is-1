import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/shared/ui/chart";
import { FetchedHumanBeing } from "../model";
import { CartesianGrid, Scatter, ScatterChart, XAxis, YAxis } from "recharts";

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
  return (
    <ChartContainer config={{}} className="min-h-[400px] w-full">
      <ScatterChart>
        <CartesianGrid />
        <XAxis dataKey="x" label="X" type="number" />
        <YAxis dataKey="y" label="Y" type="number" />
        <ChartTooltip content={<ChartTooltipContent labelKey="name" />} />
        {humans.map(it => <Scatter data={[it.coordinates]} name={it.name} fill={stringToColour(String(it.ownerId * 51324))} key={it.id} onClick={() => {
          console.log("edit id ", it.id)
        }}  />)}
      </ScatterChart>
    </ChartContainer>
  );
}

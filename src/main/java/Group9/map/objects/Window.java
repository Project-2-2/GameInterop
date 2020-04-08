package Group9.map.objects;

import Group9.map.area.ModifySpeedEffect;
import Group9.map.area.ModifyViewEffect;
import Group9.map.area.SoundEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class Window extends MapObject {

    public Window(PointContainer.Polygon area,
                double guardViewModifier, double intruderViewModifier,
                double soundRadius,
                double guardSpeedModifier, double intruderSpeedModifier) {
        super(area, Arrays.asList(
                new ModifyViewEffect(area, guardViewModifier, intruderViewModifier),
                new SoundEffect(area, SoundPerceptType.Noise, soundRadius),
                new ModifySpeedEffect(area, guardSpeedModifier, intruderSpeedModifier)
        ), ObjectPerceptType.Window);
    }

}

package Group9.map.objects;

import Group9.map.area.ModifySpeedEffect;
import Group9.map.area.ModifyViewEffect;
import Group9.map.area.SoundEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPerceptType;

public class Door extends MapObject {

    public Door(PointContainer.Polygon area,
                double guardViewModifier, double intruderViewModifier,
                double soundRadius,
                double guardSpeedModifier, double intruderSpeedModifier) {
        super(area, ObjectPerceptType.Door);
        this.addEffects(new ModifyViewEffect(this, area, guardViewModifier, intruderViewModifier),
                new SoundEffect(this, area, SoundPerceptType.Noise, soundRadius),
                new ModifySpeedEffect(this, area, guardSpeedModifier, intruderSpeedModifier));
    }

}

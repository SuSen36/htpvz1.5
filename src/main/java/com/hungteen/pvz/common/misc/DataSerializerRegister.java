package com.hungteen.pvz.common.misc;

import com.hungteen.pvz.PVZMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DataSerializerRegister {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, PVZMod.MOD_ID);

	public static final EntityDataSerializer<Vec3> VEC3 = new EntityDataSerializer.ForValueType<Vec3>() {
		@Override
		public void write(FriendlyByteBuf buf, Vec3 vec) {
			buf.writeDouble(vec.x);
			buf.writeDouble(vec.y);
			buf.writeDouble(vec.z);
		}

		@Override
		public Vec3 read(FriendlyByteBuf buf) {
			return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
	};

	public static final RegistryObject<EntityDataSerializer<Vec3>> VEC3_SERIALIZER =
			DATA_SERIALIZERS.register("vec3", () -> VEC3);
}

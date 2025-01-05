package net.emirikol.transferpet.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ContractComponent(String petName, String petUUID, String ownerName, String ownerUUID) {
	public static final Codec<ContractComponent> CODEC = RecordCodecBuilder.create(builder -> {
		return builder.group(
			Codec.STRING.fieldOf("petName").forGetter(ContractComponent::petName),
			Codec.STRING.fieldOf("petUUID").forGetter(ContractComponent::petUUID),
			Codec.STRING.fieldOf("ownerName").forGetter(ContractComponent::ownerName),
			Codec.STRING.fieldOf("ownerUUID").forGetter(ContractComponent::ownerUUID)
		).apply(builder, ContractComponent::new);
	});
}
package com.github.son_daehyeon;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
public abstract class Data {

	private UUID id;
}

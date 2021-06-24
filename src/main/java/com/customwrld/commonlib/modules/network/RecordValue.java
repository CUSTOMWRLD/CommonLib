package com.customwrld.commonlib.modules.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
public class RecordValue {
    @Getter @Setter Date date;
    @Getter @Setter String value;
}

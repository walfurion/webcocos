/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Allan G.
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class GenericDia {
    private String dia;
    private String estado;
}

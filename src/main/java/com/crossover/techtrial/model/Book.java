/**
 * 
 */
package com.crossover.techtrial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @author kshah
 *
 */
@Entity
@Table(name = "book")
@Data
public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5241781253380015253L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "title")
	@NotNull
	@Size(max = 255)
	String title;

}

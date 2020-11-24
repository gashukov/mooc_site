package com.baeldung.persistence.model;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.annotations.FilterDef;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;

@Entity
//@NormalizerDef(name = "lowercase", filters = @TokenFilterDef(factory = LowerCaseFilterFactory.class))
//@AnalyzerDef(name = "stop", filters = @TokenFilterDef(factory = StopFilterFactory.class))
@AnalyzerDef(
        name = "textanalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                @TokenFilterDef(factory = SnowballPorterFilterFactory.class,
//                        params = { @Parameter(name = “language”, value = “English”) })
        }
)
@Indexed
public class Course {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "textanalyzer"))
//    @Field(name = "title", normalizer = @Normalizer(definition = "lowercase"))
    public String title;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES, analyzer = @Analyzer(definition = "textanalyzer"))
//    @Field(name = "description", normalizer = @Normalizer(definition = "lowercase"))
    public String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

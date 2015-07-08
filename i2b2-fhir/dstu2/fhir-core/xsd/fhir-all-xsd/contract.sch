<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <!-- 
    This file contains just the constraints for the resource Contract
    It is provided for documentation purposes. When actually validating,
    always use fhir-invariants.sch (because of the way containment works)
    Alternatively you can use this file to build a smaller version of
    fhir-invariants.sch (the contents are identical; only include those 
    resources relevant to your implementation).
  -->
    <sch:pattern>
      <sch:title>Global</sch:title>
      <sch:rule context="f:*">
        <sch:assert test="@value|f:*|h:div">global-1: All FHIR elements must have a @value or children</sch:assert>
      </sch:rule>
    </sch:pattern>
  <sch:pattern>
    <sch:title>Contract</sch:title>
    <sch:rule context="f:Contract/f:meta/f:security">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:meta/f:tag">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract">
      <sch:assert test="not(exists(f:contained/f:meta/f:versionId)) and not(exists(f:contained/f:meta/f:lastUpdated))">dom-4: If a resource is contained in another resource, it SHALL not have a meta.versionId or a meta.lastUpdated</sch:assert>
      <sch:assert test="not(exists(for $id in f:contained/*/@id return $id[not(ancestor::f:contained/parent::*/descendant::f:reference/@value=concat('#', $id))]))">dom-3: If the resource is contained in another resource, it SHALL be referred to from elsewhere in the resource</sch:assert>
      <sch:assert test="not(parent::f:contained and f:contained)">dom-2: If the resource is contained in another resource, it SHALL not contain nested Resources</sch:assert>
      <sch:assert test="not(parent::f:contained and f:text)">dom-1: If the resource is contained in another resource, it SHALL not contain any narrative</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:text/f:div">
      <sch:assert test="descendant::text()[normalize-space(.)!=''] or descendant::h:img[@src]">txt-2: The narrative SHALL have some non-whitespace content</sch:assert>
      <sch:assert test="not(descendant-or-self::*[not(local-name(.)=('a', 'abbr', 'acronym', 'b', 'big', 'blockquote', 'br', 'caption', 'cite', 'code', 'colgroup', 'dd', 'dfn', 'div', 'dl', 'dt', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr', 'i', 'img', 'li', 'ol', 'p', 'pre', 'q', 'samp', 'small', 'span', 'strong', 'table', 'tbody', 'td', 'tfoot', 'th', 'thead', 'tr', 'tt', 'ul', 'var'))])">txt-1: The narrative SHALL contain only the basic html formatting elements described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
      <sch:assert test="not(descendant-or-self::*/@*[not(name(.)=('abbr', 'accesskey', 'align', 'alt', 'axis', 'bgcolor', 'border', 'cellhalign', 'cellpadding', 'cellspacing', 'cellvalign', 'char', 'charoff', 'charset', 'cite', 'class', 'colspan', 'compact', 'coords', 'dir', 'frame', 'headers', 'height', 'href', 'hreflang', 'hspace', 'id', 'lang', 'longdesc', 'name', 'nowrap', 'rel', 'rev', 'rowspan', 'rules', 'scope', 'shape', 'span', 'src', 'start', 'style', 'summary', 'tabindex', 'title', 'type', 'valign', 'value', 'vspace', 'width'))])">txt-3: The narrative SHALL contain only the basic html formatting attributes described in chapters 7-11 (except section 4 of chapter 9) and 15 of the HTML 4.0 standard, &lt;a&gt; elements (either name or href), images and internally contained style attributes</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:contained/f:meta/f:security">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:contained/f:meta/f:tag">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:contained/f:meta/f:security">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:contained/f:meta/f:tag">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:identifier/f:type">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:identifier/f:type/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:applies">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:subject">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:authority">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:domain">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:type">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:type/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:subType">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:subType/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:action">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:action/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:actionReason">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:actionReason/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:actor/f:entity">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:actor/f:role">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:actor/f:role/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:entityCodeableConcept">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:entityCodeableConcept/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:entityReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:identifier/f:type">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:identifier/f:type/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:quantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the units is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:unitPrice">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the units is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:valuedItem/f:net">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the units is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:signer/f:type">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:signer/f:party">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:identifier/f:type">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:identifier/f:type/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:applies">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:type">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:type/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:subType">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:subType/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:subject">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:action">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:action/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:actionReason">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:actionReason/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:actor/f:entity">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:actor/f:role">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:actor/f:role/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:entityCodeableConcept">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:entityCodeableConcept/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:entityReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:identifier/f:type">
      <sch:assert test="count(f:coding[f:primary/@value='true'])&lt;=1">ccc-2: Only one coding in a set can be chosen directly by the user</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:identifier/f:type/f:coding">
      <sch:assert test="not (exists(f:valueSet) and exists(f:code)) or exists(f:system)">cod-1: If a valueSet is provided, a system URI Is required</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:identifier/f:period">
      <sch:assert test="not(exists(f:start)) or not(exists(f:end)) or (f:start/@value &lt;= f:end/@value)">per-1: If present, start SHALL have a lower value than end</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:identifier/f:assigner">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:quantity">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the units is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:unitPrice">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the units is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:term/f:valuedItem/f:net">
      <sch:assert test="not(exists(f:code)) or exists(f:system)">qty-3: If a code for the units is present, the system SHALL also be present</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:bindingAttachment">
      <sch:assert test="not(exists(f:data)) or exists(f:contentType)">att-1: It the Attachment has data, it SHALL have a contentType</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:bindingReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:friendly/f:contentAttachment">
      <sch:assert test="not(exists(f:data)) or exists(f:contentType)">att-1: It the Attachment has data, it SHALL have a contentType</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:friendly/f:contentReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:legal/f:contentAttachment">
      <sch:assert test="not(exists(f:data)) or exists(f:contentType)">att-1: It the Attachment has data, it SHALL have a contentType</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:legal/f:contentReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:rule/f:contentAttachment">
      <sch:assert test="not(exists(f:data)) or exists(f:contentType)">att-1: It the Attachment has data, it SHALL have a contentType</sch:assert>
    </sch:rule>
    <sch:rule context="f:Contract/f:rule/f:contentReference">
      <sch:assert test="not(starts-with(f:reference/@value, '#')) or exists(ancestor::f:entry/f:resource/f:*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')]|/*/f:contained/f:*[f:id/@value=substring-after(current()/f:reference/@value, '#')])">ref-1: SHALL have a local reference if the resource is provided inline</sch:assert>
    </sch:rule>
    </sch:pattern>
</sch:schema>
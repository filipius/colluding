<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output omit-xml-declaration="yes" indent="no"/>

<!-- xmlstarlet sel -t -m //configuration[@id=3]/algorithms/algorithm -v . -o " " AnalysisConfiguration.xml -->

<xsl:template match="/">
	<xsl:call-template name="algorithm"/>
</xsl:template>


<xsl:template name="algorithm">
	<xsl:for-each select="//configuration[@id=3]/algorithms/algorithm">
		<!--<xsl:for-each select="algorithms/algorithm">-->
			<xsl:value-of select="."/>
			<xsl:text> </xsl:text>
<!--		</xsl:for-each>-->
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
